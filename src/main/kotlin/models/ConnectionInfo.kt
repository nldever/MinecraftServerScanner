package models

import kotlinx.serialization.Serializable

@Serializable
data class ConnectionInfo(val asn: String?, val isp: String?)