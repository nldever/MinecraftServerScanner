package managers

import LogConsole
import androidx.compose.runtime.mutableStateListOf
import cleanMotdText
import kotlinx.coroutines.*
import models.ServerInfo
import models.view.MainViewModel
import scaneServer

class MonitoringsManager(private val vm: MainViewModel) {
    private var monitoringJob: Job? = null

    private val _Monitorings = mutableStateListOf<ServerInfo>()
    val monitorings: List<ServerInfo> get() = _Monitorings

    init {
        loadMonitoring()
    }

    fun addMonitoring(server: ServerInfo) {
        if (_Monitorings.any { it.ip == server.ip && it.port == server.port }) return

        _Monitorings.add(server)
        saveMonitoringAsync()
    }

    fun removeMonitoring(server: ServerInfo) {
        val removed = _Monitorings.removeIf { it.ip == server.ip && it.port == server.port }
        if (removed) {
            saveMonitoringAsync()
        }
    }

    fun isMonitoring(server: ServerInfo): Boolean {
        return _Monitorings.any { it.ip == server.ip && it.port == server.port }
    }

    fun saveMonitoringAsync() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                MonitoringStorage.saveMonitoring(_Monitorings.toList())
            } catch (e: Exception) {
                LogConsole.error("Ошибка сохранения избранного: ${e.message}")
            }
        }
    }

    private fun loadMonitoring() {
        try {
            val loaded = MonitoringStorage.loadMonitoring()

            _Monitorings.clear()
            _Monitorings.addAll(loaded)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun startMonitoring() {

        LogConsole.debug("Запущен мониторинг серверов")

        if (monitoringJob != null) return // Уже запущено

        monitoringJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                monitorings.forEach { server ->
                    monitorMonitoringServer(server)
                }
                delay(5000)
            }
        }
    }

    private suspend fun monitorMonitoringServer(server: ServerInfo) {
        val desc = cleanMotdText(server.motd.substringBefore("by")).trim()

        val result = scaneServer(
            ip = server.ip,
            port = server.port,
            timeout = vm.currentTimeout
        )

        val index = _Monitorings.indexOfFirst {
            it.ip == server.ip && it.port == server.port
        }

        if (index == -1) return

        if (result == null) {
            LogConsole.debug("Server ${server.ip}:${server.port} не отвечает, ставим оффлайн")
            val offlineServer = server.copy(isOnline = false)
            _Monitorings[index] = offlineServer
            saveMonitoringAsync()  // <-- вот сюда!

            if (server.isOnline) {
                NotifierManager.show(
                    title = "Сервер выключен",
                    message = "⚠️ ${server.ip}:${server.port} (${desc}) больше не отвечает"
                )
            }
            saveMonitoringAsync()
        } else {
            val newMotd = result.motd
            val motdChanged = newMotd != server.motd
            val wasOffline = !server.isOnline


            if(motdChanged) {

                val updatedServer = server.copy(
                    isOnline = true,
                )

                _Monitorings[index] = updatedServer

                ServerMonitoringManager.addToHistory(result)

            } else {
                val updatedServer = server.copy(
                    isOnline = true,
                    onlinePlayers = result.onlinePlayers,
                    players = result.players,
                    motd = newMotd,
                    favicon = result.favicon
                )

                _Monitorings[index] = updatedServer
            }


            if (wasOffline) {
                NotifierManager.show(
                    title = "Сервер включён",
                    message = "✅ ${server.ip}:${server.port} (${desc}) снова доступен!" +
                            if (motdChanged) "\nНовая карта: ${cleanMotdText(newMotd.substringBefore("by")).trim()}" else ""
                )
            }


            saveMonitoringAsync()
        }

        saveMonitoringAsync()
    }
}