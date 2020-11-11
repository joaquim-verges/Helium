package com.joaquimverges.kmp.news.android.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.drawLayer
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.unit.dp

@Composable
fun Draggable(onSwiped: () -> Unit, children: @Composable () -> Unit) {
    val offsetPosition = remember { mutableStateOf(0f) }
    val screenWidth = with(ContextAmbient.current) {
        resources.displayMetrics.widthPixels
    }
    val max = screenWidth.toFloat() * .2f
    val min = 0f
    Box {
        Column(
            Modifier.width(with(DensityAmbient.current) { max.toDp() })
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                asset = Icons.Filled.KeyboardArrowLeft,
                modifier = Modifier.size(64.dp)
                    .drawLayer(
                        scaleX = offsetPosition.value / max,
                        scaleY = offsetPosition.value / max,
                        alpha = offsetPosition.value / max
                    )
            )
        }

        Surface(
            Modifier.draggable(
                orientation = Orientation.Horizontal,
                onDrag = { delta ->
                    val coeff = 1f - (offsetPosition.value / max)
                    val newValue = offsetPosition.value + (delta * coeff)
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
}
