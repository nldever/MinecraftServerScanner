package managers

import MainViewModel
import androidx.compose.material.Colors
import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import models.ServerInfo
import scaneServer
import toHex

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
        try {
            scaneServer(
                ip = server.ip,
                port = server.port,
                timeout = vm.currentTimeout
            )
        } catch (_: Exception) {
            NotifierManager.show(
                title = "Сервер выключен",
                message = "⚠️ ${server.ip}:${server.port} больше не отвечает"
            )
            removeFavorite(server)
        }
    }

}