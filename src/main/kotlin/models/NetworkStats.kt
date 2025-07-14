package models

import LogConsole

object NetworkStats {
    private var sentBytes = 0L
    private var receivedBytes = 0L


    fun addSent(num: Long) {
        sentBytes += num
    }
    fun addReceived(num: Long) {
        receivedBytes += num
    }

    fun getSent(): Long {
        return sentBytes
    }
    fun getReceived(): Long {
        return receivedBytes
    }

    fun reset() {
        sentBytes = 0
        receivedBytes = 0
    }

    fun getTotalUsage(): Long = sentBytes + receivedBytes
}