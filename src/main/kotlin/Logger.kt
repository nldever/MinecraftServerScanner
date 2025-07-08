import androidx.compose.runtime.mutableStateListOf

object Logger {
    val logs = mutableStateListOf<String>()

    fun log(message: String) {
        logs.add(message)
    }
}