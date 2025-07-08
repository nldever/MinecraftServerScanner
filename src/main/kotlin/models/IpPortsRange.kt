package models

import kotlinx.serialization.Serializable


@Serializable
data class IpPortsRange(
    val ip: String,
    val portsStart: Int,
    val portsEnd: Int
)