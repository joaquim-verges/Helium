package com.joaquimverges.kmp.news.android.utils

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt


@Composable
fun <T> StackTransition(
    targetScreen: T,
    shouldReverseAnimation: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    val state by remember {
        mutableStateOf(
            InnerState(
                targetScreen,
                mutableListOf()
            )
        )
    }
    val targetChanged = (targetScreen != state.current)
    if (targetChanged || state.items.isEmpty()) {
        state.current = targetScreen
        // Only manipulate the list when the state is changed, or in the first run.
        val screenInfos = state.items.map { it.screenAndTransitionState }
            .run {
                if (find { it.screen == targetScreen } == null) {
                    // if target screen not already added, add it with a default transition state
                    toMutableList().also {
                        it.add(
                            ScreenAndTransitionState(
                                targetScreen,
                                createTransitionState()
                            )
                        )
                    }
                } else {
                    this
                }
            }
        state.items.clear()
        // map existing keys (previous screen + new screen) to new AnimationItems
        // which has draw + transition info embedded
        screenInfos.mapTo(state.items) { screenAndTransitionState ->
            val screen = screenAndTransitionState.screen
            val transitionState = screenAndTransitionState.transitionState
            // determine new visibility (appearing or disappearing)
            val visible = screen == targetScreen
            val targetVisibilityVisibility = when {
                visible -> ScreenVisibility.Visible
                else -> ScreenVisibility.BecomingNotVisible
            }
            // set the new target visibility to the existing state
            transitionState.targetState = targetVisibilityVisibility

            // Create the Animation with updated transition state
            AnimationItem(screenAndTransitionState) {

                // this will actually create and update the animation values at every frame
                val transition = updateTransition(transitionState)
                val transitionInfo = updateTransitionInfo(
                    transition = transition,
                    shouldReverseAnimation = shouldReverseAnimation
                )
                // Defines how to draw the screen with an animated container
                Box(
                    Modifier
                        .alpha(transitionInfo.alpha)
                        .scale(transitionInfo.scale)
                        .offset {
                            IntOffset(
                                transitionInfo.offsetX.roundToInt(),
                                0
                            )
                        }
                ) {
                    content(screen)
                }
            }
        }
    }

    // remove non-visible items with finished transition
    state.items.removeAll {
        it.screenAndTransitionState.transitionState.currentState == it.screenAndTransitionState.transitionState.targetState
            && it.screenAndTransitionState.screen != state.current
    }

    Box(modifier) {
        // iterate over all items to draw with the animated container
        state.items.forEach {
            key(it.screenAndTransitionState.screen) {
                it.content()
            }
        }
    }
}

@Composable
private fun createTransitionState(): MutableTransitionState<ScreenVisibility> {
    // create a default transition state to be re-used across compositions
    return remember {
        MutableTransitionState(ScreenVisibility.BecomingVisible)
    }
}

@Composable
private fun updateTransitionInfo(
    transition: Transition<ScreenVisibility>,
    shouldReverseAnimation: Boolean,
    animationSpec: FiniteAnimationSpec<Float> = tween(durationMillis = 300),
): TransitionInfo {
    val transitionOffset =
        with(LocalContext.current) {
            with(LocalDensity.current) {
                resources.configuration.screenWidthDp * density
            }
        }

    // the actual transition definition, just needs the target values for each state
    val alpha by transition.animateFloat(transitionSpec = { animationSpec }) {
        when (it) {
            ScreenVisibility.Visible -> 1f
            ScreenVisibility.BecomingNotVisible -> 0f
            ScreenVisibility.BecomingVisible -> if (shouldReverseAnimation) 0f else 1f
        }
    }
    val offsetX by transition.animateFloat(transitionSpec = { animationSpec }) {
        when (it) {
            ScreenVisibility.Visible -> 0f
            ScreenVisibility.BecomingNotVisible -> if (shouldReverseAnimation) transitionOffset else 0f
            ScreenVisibility.BecomingVisible -> if (shouldReverseAnimation) 0f else transitionOffset
        }
    }
    val scale by transition.animateFloat(transitionSpec = { animationSpec }) {
        when (it) {
            ScreenVisibility.Visible -> 1f
            ScreenVisibility.BecomingNotVisible -> if (shouldReverseAnimation) 1f else .8f
            ScreenVisibility.BecomingVisible -> if (shouldReverseAnimation) .8f else 1f
        }
    }

    return TransitionInfo(
        alpha,
        offsetX,
        scale
    )
}

private data class InnerState<T>(
    var current: T,
    val items: MutableList<AnimationItem<T>>
)

private data class ScreenAndTransitionState<T>(
    val screen: T,
    val transitionState: MutableTransitionState<ScreenVisibility>
)

enum class ScreenVisibility {
    Visible, BecomingNotVisible, BecomingVisible,
}

private data class TransitionInfo(
    val alpha: Float,
    val offsetX: Float,
    val scale: Float
)

private data class AnimationItem<T>(
    val screenAndTransitionState: ScreenAndTransitionState<T>,
    val content: @Composable () -> Unit
)
