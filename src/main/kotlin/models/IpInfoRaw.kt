package models

import kotlinx.serialization.Serializable

@Serializable
data class IpInfoRaw(
    val success: Boolean,
    val ip: String,
    val city: String?,
    val region: String?,
    val country: String?,
    val org: String?,
    val latitude: Double?,
    val longitude: Double?,
    val reverse: String?,
    val timezone: TimezoneInfo?,
    val connection: ConnectionInfo?
)