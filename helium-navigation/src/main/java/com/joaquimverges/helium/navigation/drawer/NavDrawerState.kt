package com.joaquimverges.helium.navigation.drawer

import com.joaquimverges.helium.core.state.BlockState

/**
 * @author joaquim
 */
sealed class NavDrawerState : BlockState {
    object Opened : NavDrawerState()
    object Closed : NavDrawerState()
}