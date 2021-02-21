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
    // state remembers which screen we're trying to show, as well as a list of AnimationItems
    // AnimationItem is a composable that wraps the screen in an animated container
    // we keep at most 2 AnimationItems during a transition, to draw one appearing and one disappearing
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
        // if changed, or first draw, trigger a composition by mutating the state
        state.current = targetScreen
        // add the new target screen if not already present and copy to a new list
        // in this new list we're just interested in the screen and its current transition state
        // not the actual composable content (we'll need to update it for the new transition)
        val screenInfos = state.items.map { it.screenAndTransitionState }
            .run {
                if (find { it.screen == targetScreen } == null) {
                    // if target screen not already there, add it with a default transition state
                    toMutableList().also {
                        it.add(
                            ScreenAndTransitionState(
                                targetScreen,
                                createTransitionState()
                            )
                        )
                    }
                } else {
                    // otherwise just use the existing list
                    this
                }
            }
        // clear state items, we'll rebuild the list with new items to draw below
        state.items.clear()
        // map the screen info list (current screen + new screen) to new AnimationItems
        // which has draw + transition info embedded, and add them to the state
        screenInfos.mapTo(state.items) { screenAndTransitionState ->
            val screen = screenAndTransitionState.screen
            val transitionState = screenAndTransitionState.transitionState
            // determine new visibility (appearing or disappearing)
            val targetVisibility = when (screen) {
                targetScreen -> ScreenVisibility.Visible
                else -> ScreenVisibility.BecomingNotVisible
            }
            // set the new target visibility to the existing transition state
            transitionState.targetState = targetVisibility

            // Create AnimationItem with updated transition state (and add it to the state)
            AnimationItem(screenAndTransitionState) {

                // this will actually create and update the animation values at every frame
                val transition = updateTransition(transitionState)
                val transitionInfo = updateTransitionInfo(
                    transition = transition,
                    shouldReverseAnimation = shouldReverseAnimation
                )
                // Define how to draw the screen with the animated container
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
        // iterate over the AnimationItems to draw screens with their animated containers
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
