import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import functions.getIpInfo
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.serialization.json.Json
import models.IpInfo
import models.IpPortsRange
import models.MotdJson
import models.ServerInfo
import net.lenni0451.mcping.MCPing
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.net.InetSocketAddress
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import javax.imageio.ImageIO
import kotlin.math.roundToInt

val timeoutCache = mutableMapOf<String, Int>()
val ipInfoCache = mutableMapOf<String, IpInfo?>()

suspend fun pingMinecraftServer(ip: String, port: Int, timeoutMs: Int = 500): ServerInfo {
    return try {
        val response = MCPing.pingModern()
            .address(InetSocketAddress(ip, port))
            .timeout(timeoutMs, timeoutMs)
            .getSync()

        // Получение и обработка иконки
        val faviconBase64 = response.favicon.orEmpty()

        // Извлечение информации
        val versionName = response.version?.name.orEmpty()
        val ping = response.ping.toInt()
        val serverType = detectServerType(versionName)
        val motd = response.description.orEmpty()

        // Лог успеха
        LogConsole.online("$ip:$port online")

        // Кэшированное получение страны

        val country = ipInfoCache.getOrPut(ip) {
            getIpInfo(ip)
        }

        val location = country?.let {
            "${it.country}/${it.city} (${it.region})"
        } ?: "Unknown"

        // Возврат успешного результата
        ServerInfo(
            ip = ip,
            port = port,
            isOnline = true,
            motd = motd,
            onlinePlayers = response.players?.online ?: 0,
            maxPlayers = response.players?.max ?: 0,
            version = versionName,
            players = response.players?.sample?.mapNotNull { it.name } ?: emptyList(),
            country = location,
            ping = ping,
            serverType = serverType,
            favicon = faviconBase64
        )

    } catch (e: Exception) {
        // Лог ошибки
        LogConsole.offline("$ip:$port")
        ServerInfo(ip = ip, port = port, isOnline = false)
    }
}

fun detectServerType(versionName: String): String {
    val lower = versionName.lowercase()
    return when {
        "paper" in lower -> "Paper"
        "spigot" in lower -> "Spigot"
        "bukkit" in lower -> "Bukkit"
        "forge" in lower -> "Forge"
        "fabric" in lower -> "Fabric"
        else -> "Vanilla"
    }
}


fun getTagsFromMotd(motd: String): List<String> {
    val lowerMotd = motd.lowercase()

    val tags = mutableSetOf<String>()

    val keywordMap = mapOf(
        "parkour" to "Паркур",
        "bedwars" to "Мини-игра",
        "skywars" to "Мини-игра",
        "eggwars" to "Мини-игра",
        "survival" to "Выживание",
        "anarchy" to "Анархия",
        "vanilla" to "Vanilla",
        "creative" to "Креатив",
        "rpg" to "RPG",
        "quest" to "Квест",
        "minigame" to "Мини-игра",
        "wars" to "Мини-игра",
        "faction" to "Фракции",
        "hardcore" to "Хардкор",
        "adventure" to "Приключения",
        "pvp" to "PvP",
        "pve" to "PvE",
        "find the" to "Прохождение",
        "dropper" to "Дроппер",
        "roleplay" to "Ролевая",
        "lifesteal" to "Lifesteal",
        "prison" to "Prison",
        "skyblock" to "SkyBlock",
        "blockhunt" to "Мини-игра",
        "parkour spiral" to "Паркур",
        "builder" to "Строительство"
    )

    for ((keyword, tag) in keywordMap) {
        if (lowerMotd.contains(keyword)) {
            tags += tag
        }
    }

    return tags.toList()
}

fun stripFormatting(text: String, maxWords: Int = 3): String {
    return text.replace(Regex("§."), "")
        .split(Regex("\\s+"))         // разбиваем по пробелам
        .take(maxWords)               // берём первые maxWords слов
        .joinToString(" ")            // собираем обратно
        .trim()
}

suspend fun scaneServer(
    ip: String,
    port: Int,
    timeout: Int
): ServerInfo? = withContext(Dispatchers.IO) {
    val result = pingMinecraftServer(ip, port, timeout)
    return@withContext if (result.isOnline) result else null
}

fun Color.toHex(): String {
    return String.format(
        "#%02X%02X%02X",
        (red * 255).toInt(),
        (green * 255).toInt(),
        (blue * 255).toInt()
    )
}


fun cleanMotdText(rawMotd: String): String {
    val noSectionSign = rawMotd.replace(Regex("§."), "")

    return try {
        val json = Json { ignoreUnknownKeys = true }
        val motdObj = json.decodeFromString<MotdJson>(noSectionSign)
        val builder = StringBuilder()
        fun extractText(m: MotdJson) {
            m.text?.let { builder.append(it) }
            m.extra?.forEach { extractText(it) }
        }
        extractText(motdObj)
        val textResult = builder.toString()
        textResult.ifBlank { noSectionSign }
    } catch (e: Exception) {
        noSectionSign
    }
}

suspend fun scanServers(
    ips: List<IpPortsRange>,
    timeout: Int,
    parallelLimit: Int,
    stopCondition: () -> Boolean,
    onProgress: (Float, String) -> Unit
): List<ServerInfo> {

    val onlineServers = mutableListOf<ServerInfo>()

    val total = ips.sumOf { it.portsEnd - it.portsStart + 1 }
    val completed = AtomicInteger(0)
    val semaphore = Semaphore(parallelLimit)

    coroutineScope {
        val jobs = ips.flatMap { ipRange ->

            LogConsole.info("Начало сканирования ${ipRange.ip}, с ${ipRange.portsStart} до ${ipRange.portsEnd} порта")

            (ipRange.portsStart..ipRange.portsEnd).map { port ->
                async(Dispatchers.IO) {
                    if (stopCondition()) return@async
                    semaphore.withPermit {
                        if (stopCondition()) return@withPermit
                        val result = pingMinecraftServer(ipRange.ip, port, timeout)
                        if (result.isOnline) {
                            synchronized(onlineServers) {
                                onlineServers.add(result)
                            }
                        }
                        val done = completed.incrementAndGet()
                        if (done % 20 == 0 || done == total) {
                            val percent = done.toFloat() / total
                            withContext(Dispatchers.Main) {
                                onProgress(percent, "Сканировано: ${(percent * 100).roundToInt()}%")
                            }
                        }
                    }
                }
            }
        }
        jobs.awaitAll()
    }

    return onlineServers
}


fun parseMinecraftColoredJson(jsonString: String, isDarkTheme: Boolean): AnnotatedString {
    return try {
        val json = JSONObject(jsonString)
        val builder = AnnotatedString.Builder()

        val text = json.optString("text", "")
        if (text.isNotEmpty()) {
            builder.append(parseMinecraftColoredText(text, isDarkTheme))
        } else {
            val extra = json.optJSONArray("extra") ?: JSONArray()
            for (i in 0 until extra.length()) {
                val elem = extra.getJSONObject(i)
                val elemText = elem.optString("text", "")
                builder.append(parseMinecraftColoredText(elemText, isDarkTheme))
            }
        }

        builder.toAnnotatedString()
    } catch (e: Exception) {
        parseMinecraftColoredText(
            jsonString,
            isDarkTheme = isDarkTheme
        )
    }
}

fun parseMinecraftColoredText(text: String, isDarkTheme: Boolean): AnnotatedString {
    val defaultColor = if (isDarkTheme) Color.White else Color.Black

    val colorMap = mapOf(
        '0' to Color(0xFF1B1B1B), // black
        '1' to Color(0xFF3B6EFF), // dark blue
        '2' to Color(0xFF39C16C), // dark green
        '3' to Color(0xFF00CCCC), // dark aqua
        '4' to Color(0xFFE03C3C), // dark red
        '5' to Color(0xFFC969E0), // dark purple
        '6' to Color(0xFFF7A93F), // gold
        '7' to if (isDarkTheme) Color(0xFFCCCCCC) else Color(0xFF444444), // gray
        '8' to if (isDarkTheme) Color(0xFF777777) else Color(0xFF888888), // dark gray
        '9' to Color(0xFF3FB1FF), // blue
        'a' to Color(0xFF6EE76E), // green
        'b' to Color(0xFF51EBEB), // aqua
        'c' to Color(0xFFFF5C5C), // red
        'd' to Color(0xFFF78CFF), // light purple
        'e' to Color(0xFFFFE564), // yellow
        'f' to defaultColor, // white
        'r' to defaultColor  // reset
    )

    val builder = AnnotatedString.Builder()
    var currentColor = defaultColor
    var i = 0

    while (i < text.length) {
        if (text[i] == '§' && i + 1 < text.length) {
            val code = text[i + 1].lowercaseChar()
            currentColor = colorMap[code] ?: currentColor
            i += 2
        } else {
            builder.withStyle(SpanStyle(color = currentColor)) {
                append(text[i])
            }
            i++
        }
    }

    return builder.toAnnotatedString()
}

fun parseIpPorts(input: String): List<IpPortsRange> {
    return input.lines()
        .mapNotNull { line ->
            val parts = line.split(':')
            if (parts.size != 2) return@mapNotNull null

            val ip = parts[0].trim()
            val rangeParts = parts[1].split("..")
            if (rangeParts.size != 2) return@mapNotNull null

            val start = rangeParts[0].toIntOrNull() ?: return@mapNotNull null
            val end = rangeParts[1].toIntOrNull() ?: return@mapNotNull null

            IpPortsRange(ip, start, end)
        }
}

fun decodeFavicon(faviconBase64: String?): ImageBitmap? {
    if (faviconBase64.isNullOrBlank()) return null
    return try {
        val base64Data = faviconBase64.removePrefix("data:image/png;base64,")
        val imageBytes = Base64.getDecoder().decode(base64Data)
        val bufferedImage = ImageIO.read(ByteArrayInputStream(imageBytes))
        bufferedImage.toComposeImageBitmap()
    } catch (e: Exception) {
        null
    }
}

fun getFlagEmojiFromCountryName(countryName: String): String {
    val countryCode = countryNameToCode[countryName.lowercase()] ?: return "🏳️"
    return getFlagEmojiFromCountryCode(countryCode)
}

val countryNameToCode = mapOf(
    "russia" to "RU",
    "united states" to "US",
    "germany" to "DE",
    "france" to "FR",
    "china" to "CN",
    "japan" to "JP",
    "ukraine" to "UA",
    "brazil" to "BR",
    "india" to "IN",
    "united kingdom" to "GB",
    "canada" to "CA",
    "australia" to "AU",
    "poland" to "PL",
    "netherlands" to "NL"
)

fun getFlagEmojiFromCountryCode(code: String): String {
    if (code.length != 2) return "🏳️"
    val upperCode = code.uppercase()
    val firstChar = Character.codePointAt(upperCode, 0) - 'A'.code + 0x1F1E6
    val secondChar = Character.codePointAt(upperCode, 1) - 'A'.code + 0x1F1E6
    return String(Character.toChars(firstChar)) + String(Character.toChars(secondChar))
}

fun extractAndJoinBeforeBy(motd: AnnotatedString): String {
    val text = motd.text
    val index = text.indexOf("by")
    if (index == -1) return "" 
    val substring = text.substring(0, index)
    return substring.split("\\s+".toRegex())
        .filter { it.isNotEmpty() }
        .joinToString(separator = "")
}