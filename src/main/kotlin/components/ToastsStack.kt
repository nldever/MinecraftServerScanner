package components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import managers.ToastManager

@Composable
fun ToastsStack(toastManager: ToastManager, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(bottom = 24.dp, end = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.End
    ) {
        toastManager.toasts.forEach { toast ->
            key(toast.id) {
                var visible by remember { mutableStateOf(true) }

                LaunchedEffect(toast) {
                    // Ждём, затем плавно скрываем
                    delay(toast.durationMillis - 400)
                    visible = false
                    // Можно потом ещё вызвать удаление из менеджера
                }

                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
                        initialOffsetY = { it / 2 },
                        animationSpec = tween(300)
                    ),
                    exit = fadeOut(animationSpec = tween(400)) + slideOutVertically(
                        targetOffsetY = { it / 2 },
                        animationSpec = tween(400)
                    )
                ) {
                    ToastMessage(
                        message = toast.message,
                        onDismiss = {
                            // Удаление уведомления по нажатию, если нужно
                            toastManager.removeToast(toast)
                        }
                    )
                }
            }
        }
    }
}