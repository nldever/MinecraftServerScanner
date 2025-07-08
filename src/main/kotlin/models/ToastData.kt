package models

data class ToastData(
    val id: Int = (Math.random() * Int.MAX_VALUE).toInt(),
    val message: String,
    val durationMillis: Long = 2500L
)