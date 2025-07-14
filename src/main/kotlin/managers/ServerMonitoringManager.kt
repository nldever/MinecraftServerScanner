package managers

import androidx.compose.runtime.mutableStateListOf
import models.ServerInfo
import models.ServerMonitoring

object ServerMonitoringManager {
    private val _monitoring = mutableStateListOf<ServerMonitoring>()
    val monitoring: List<ServerMonitoring> get() = _monitoring

    fun addServer(server: ServerInfo) {
        if (_monitoring.any { it.server.ip == server.ip && it.server.port == server.port }) return
        _monitoring.add(ServerMonitoring(server, history = listOf()))
    }

    fun addToHistory(server: ServerInfo) {
        val index = _monitoring.indexOfFirst { it.server.ip == server.ip && it.server.port == server.port }
        if (index == -1) {
            _monitoring.add(ServerMonitoring(server, history = listOf()))
        } else {
            val old = _monitoring[index]
            val newHistory = old.history + server
            _monitoring[index] = ServerMonitoring(old.server, history = newHistory)
        }
    }

    fun updateServer(updated: ServerInfo) {
        val index = _monitoring.indexOfFirst { it.server.ip == updated.ip && it.server.port == updated.port }
        if (index == -1) {
            // Если нет — добавляем новый
            _monitoring.add(ServerMonitoring(updated, history = listOf()))
        } else {
            val old = _monitoring[index]
            val newHistory = old.history + old.server // добавляем старый в историю
            _monitoring[index] = ServerMonitoring(updated, history = newHistory)
        }
    }

    fun contains(ip: String, port: Int): Boolean {
        return _monitoring.any { it.server.ip == ip && it.server.port == port }
    }

    fun removeServer(server: ServerInfo) {
        _monitoring.removeAll { it.server.ip == server.ip && it.server.port == server.port }
    }

    fun getCurrent(server: ServerInfo): ServerInfo? =
        _monitoring.find { it.server.ip == server.ip && it.server.port == server.port }?.server

    fun getHistory(server: ServerInfo): List<ServerInfo> =
        _monitoring.find { it.server.ip == server.ip && it.server.port == server.port }?.history ?: emptyList()
}
