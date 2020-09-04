package com.joaquimverges.kmp.news.android.utils

import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.offsetPx
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.unit.dp

@Composable
fun Draggable(onSwiped: () -> Unit, children: @Composable () -> Unit) {
    val offsetPosition = remember { mutableStateOf(0f) }
    val screenWidth = with(ContextAmbient.current) {
        resources.displayMetrics.widthPixels
    }
    val max = screenWidth.toFloat() * .25f
    val min = 0f
    Surface(
        Modifier.draggable(
            orientation = Orientation.Horizontal,
            onDrag = { delta ->
                val newValue = offsetPosition.value + delta
                offsetPosition.value = newValue.coerceIn(min, max)
            },
            onDragStopped = { velocity ->
                if (velocity > 0 || offsetPosition.value > max * .5f) {
                    onSwiped()
                } else {
                    offsetPosition.value = 0f
                }
            }
        ).offsetPx(x = offsetPosition),
        elevation = 8.dp
    ) {
        children()
    }
}
