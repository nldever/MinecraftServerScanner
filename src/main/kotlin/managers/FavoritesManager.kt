package managers

import models.view.MainViewModel
import androidx.compose.runtime.mutableStateListOf
import cleanMotdText
import extractAndJoinBeforeBy
import kotlinx.coroutines.*
import models.ServerInfo
import scaneServer
import stripFormatting

class FavoritesManager(private val vm: MainViewModel) {
    private var monitoringJob: Job? = null

    private val _favorites = mutableStateListOf<ServerInfo>()
    val favorites: List<ServerInfo> get() = _favorites

    init {
        loadFavorites()
    }

    fun addFavorite(server: ServerInfo) {
        if (_favorites.any { it.ip == server.ip && it.port == server.port }) return

        _favorites.add(server)
        saveFavoritesAsync()
    }

    fun removeFavorite(server: ServerInfo) {
        val removed = _favorites.removeIf { it.ip == server.ip && it.port == server.port }
        if (removed) {
            saveFavoritesAsync()
        }
    }

    fun isFavorite(server: ServerInfo): Boolean {
        return _favorites.any { it.ip == server.ip && it.port == server.port }
    }

    fun saveFavoritesAsync() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                FavoriteStorage.saveFavorites(_favorites.toList())
            } catch (e: Exception) {
                LogConsole.error("Ошибка сохранения избранного: ${e.message}")
            }
        }
    }

    private fun loadFavorites() {
        try {
            val loaded = FavoriteStorage.loadFavorites()

            _favorites.clear()
            _favorites.addAll(loaded)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun startFavoritesMonitoring() {

        LogConsole.debug("Запущен мониторинг серверов")

        if (monitoringJob != null) return // Уже запущено

        monitoringJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                favorites.forEach { server ->
                    monitorFavoriteServer(server)
                }
                delay(5000)
            }
        }
    }

    private suspend fun monitorFavoriteServer(server: ServerInfo) {
        val desc = cleanMotdText(server.motd.substringBefore("by")).trim()

        val result = scaneServer(
            ip = server.ip,
            port = server.port,
            timeout = vm.currentTimeout
        )

        val index = _favorites.indexOfFirst {
            it.ip == server.ip && it.port == server.port
        }

        if (index == -1) return

        if (result == null) {
            LogConsole.debug("Server ${server.ip}:${server.port} не отвечает, ставим оффлайн")
            val offlineServer = server.copy(isOnline = false)
            _favorites[index] = offlineServer
            saveFavoritesAsync()  // <-- вот сюда!

            if (server.isOnline) {
                NotifierManager.show(
                    title = "Сервер выключен",
                    message = "⚠️ ${server.ip}:${server.port} (${desc}) больше не отвечает"
                )
            }
            saveFavoritesAsync()
        } else {
            val newMotd = result.motd
            val motdChanged = newMotd != server.motd
            val wasOffline = !server.isOnline


            if(motdChanged) {

                ServerMonitoringManager.addToHistory(result)

            } else {
                val updatedServer = server.copy(
                    isOnline = true,
                    onlinePlayers = result.onlinePlayers,
                    players = result.players,
                    motd = newMotd,
                    favicon = result.favicon
                )

                _favorites[index] = updatedServer
            }


            if (wasOffline) {
                NotifierManager.show(
                    title = "Сервер включён",
                    message = "✅ ${server.ip}:${server.port} (${desc}) снова доступен!" +
                            if (motdChanged) "\nНовая карта: ${cleanMotdText(newMotd.substringBefore("by")).trim()}" else ""
                )
            }


            saveFavoritesAsync()
        }

        saveFavoritesAsync()
    }
}