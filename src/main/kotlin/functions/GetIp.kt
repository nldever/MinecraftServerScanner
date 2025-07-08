package functions

import LogConsole
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.serialization.json.Json
import models.IpInfo

val client = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
        })
    }
    expectSuccess = false // важно: не кидай исключения на кодах 4xx/5xx
}

val geoSemaphore = Semaphore(10)

suspend fun getIpInfo(ip: String): IpInfo? = geoSemaphore.withPermit {
    try {
        val info: IpInfo = client.get("https://ipwho.is/$ip?lang=ru").body()
        if (info.success) info else null
    } catch (e: Exception) {
        LogConsole.error("Ошибка получения IP-инфо: ${e.message}")
        null
    }
}