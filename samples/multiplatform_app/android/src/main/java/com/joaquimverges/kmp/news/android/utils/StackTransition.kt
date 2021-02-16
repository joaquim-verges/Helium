package com.joaquimverges.kmp.news.android.utils

/**
@Composable
fun StackTransition(
    state: AppRouter.Screen?,
    shouldReverseAnimation: Boolean,
    children: @Composable (AppRouter.Screen?) -> Unit
) {
    val transitionOffset = with(LocalContext.current) {
        with(LocalDensity.current) {
            resources.configuration.screenWidthDp * density
        }
    }
    ItemSwitcher(
        current = state,
        transitionDefinition = createTransitionDefinition(
            offsetPx = transitionOffset,
            reverse = shouldReverseAnimation
        ),
    ) { current, transitionState ->
        Surface(
            Modifier
                .drawLayer(
                    scaleX = transitionState[Scale],
                    scaleY = transitionState[Scale],
                    translationX = transitionState[Offset],
                    alpha = transitionState[Alpha]
                )
        ) {
            children(current)
        }
    }
}

private val Alpha = FloatPropKey()
private val Offset = FloatPropKey()
private val Scale = FloatPropKey()
private const val DelayForContentToLoad = 0

@Composable
private fun createTransitionDefinition(
    duration: Int = 500,
    offsetPx: Float,
    reverse: Boolean = false
): TransitionDefinition<ItemTransitionState> = remember(reverse, offsetPx, duration) {
    transitionDefinition {
        state(ItemTransitionState.Visible) {
            this[Alpha] = 1f
            this[Offset] = 0f
            this[Scale] = 1f
        }
        state(ItemTransitionState.BecomingVisible) {
            this[Alpha] = if (reverse) 0f else 1f
            this[Offset] = if (reverse) 0f else offsetPx
            this[Scale] = if (reverse) .8f else 1f
        }
        state(ItemTransitionState.BecomingNotVisible) {
            this[Alpha] = 0f
            this[Offset] = if (reverse) offsetPx else 0f
            this[Scale] = if (reverse) 1f else .8f
        }

        val halfDuration = duration / 2
        val quarterDuration = duration / 2

        transition(
            fromState = ItemTransitionState.BecomingVisible,
            toState = ItemTransitionState.Visible
        ) {
            Alpha using tween(
                durationMillis = halfDuration,
                delayMillis = DelayForContentToLoad,
                easing = LinearEasing
            )
            Offset using tween(
                durationMillis = halfDuration,
                delayMillis = DelayForContentToLoad,
                easing = LinearOutSlowInEasing
            )
            Scale using tween(
                durationMillis = halfDuration,
                delayMillis = DelayForContentToLoad,
                easing = LinearOutSlowInEasing
            )
        }

        transition(
            fromState = ItemTransitionState.Visible,
            toState = ItemTransitionState.BecomingNotVisible
        ) {
            Alpha using tween(
                durationMillis = quarterDuration,
                easing = LinearEasing,
                delayMillis = DelayForContentToLoad
            )
            Offset using tween(
                durationMillis = quarterDuration,
                easing = LinearOutSlowInEasing,
                delayMillis = DelayForContentToLoad
            )
            Scale using tween(
                durationMillis = quarterDuration,
                easing = LinearOutSlowInEasing,
                delayMillis = DelayForContentToLoad
            )
        }
    }
}
**/