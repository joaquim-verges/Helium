package com.joaquimverges.helium.navigation.state

import com.joaquimverges.helium.core.state.BlockState

/**
 * @author joaquim
 */
sealed class NavDrawerState : BlockState {
    object Opened : NavDrawerState()
    object Closed : NavDrawerState()
}