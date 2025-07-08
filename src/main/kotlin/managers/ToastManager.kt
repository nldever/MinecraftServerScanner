package managers

import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import models.ToastData

class ToastManager {
    companion object {
        val defaultManager = ToastManager()
    }

    private val _toasts = mutableStateListOf<ToastData>()
    val toasts: List<ToastData> = _toasts

    @OptIn(DelicateCoroutinesApi::class)
    fun showToast(message: String, duration: Long = 2500L) {
        val toast = ToastData(message = message, durationMillis = duration)
        _toasts.add(toast)
        kotlinx.coroutines.GlobalScope.launch {
            kotlinx.coroutines.delay(duration)
            _toasts.remove(toast)
        }
    }

    fun removeToast(toast: ToastData) {
        _toasts.remove(toast)
    }
}