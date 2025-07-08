package models

import kotlinx.serialization.Serializable

@Serializable
data class ServerInfo(
    val ip: String,
    val port: Int,
    val isOnline: Boolean = false,
    val motd: String = "",
    val onlinePlayers: Int = 0,
    val maxPlayers: Int = 0,
    val players: List<String> = listOf(),
    val version: String = "",
    val country: String = "",
    val ping: Int = 0,
    val serverType: String = "",
    val favicon: String = "",
)