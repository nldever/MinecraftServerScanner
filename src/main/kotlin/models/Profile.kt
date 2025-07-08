package models

import kotlinx.serialization.Serializable
import themes.AppTheme

@Serializable
data class Profile(
    val name: String,
    val timeout: Int,
    val parallels: Int,
    val theme: AppTheme,
    val targetIps: List<IpPortsRange>
)