package models

data class ServerMonitoring(
    val server: ServerInfo,
    val history: List<ServerInfo>
)
