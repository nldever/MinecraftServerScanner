package models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IpInfo(
    val ip: String,
    val success: Boolean,
    val country: String? = null,
    val region: String? = null,
    val city: String? = null,
    val isp: String? = null,
    val asn: Int? = null,
    @SerialName("reverse") val reverseDns: String? = null
)