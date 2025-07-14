package managers

object NetworkStats {
    var sentBytes = 0L
    var receivedBytes = 0L

    fun reset() {
        sentBytes = 0
        receivedBytes = 0
    }

    fun getTotalUsage(): Long = sentBytes + receivedBytes
}