package com.joaquimverges.helium.navigation.state

import com.joaquimverges.helium.core.state.ViewState

/**
 * @author joaquim
 */
sealed class NavDrawerState : ViewState {
    object Opened: NavDrawerState()
    object Closed: NavDrawerState()
}