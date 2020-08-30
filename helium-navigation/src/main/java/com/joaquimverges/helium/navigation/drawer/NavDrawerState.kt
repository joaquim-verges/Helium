package com.joaquimverges.helium.navigation.drawer

import com.joaquimverges.helium.core.state.BlockState

/**
 * Different states rendered by a [NavDrawerUi]
 */
sealed class NavDrawerState : BlockState {
    object Opened : NavDrawerState()
    object Closed : NavDrawerState()
}
