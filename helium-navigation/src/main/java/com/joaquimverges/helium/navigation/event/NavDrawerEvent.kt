package com.joaquimverges.helium.navigation.event

import com.joaquimverges.helium.core.event.ViewEvent

/**
 * User events coming from a [NavDrawerViewDelegate]
 */
sealed class NavDrawerEvent : ViewEvent {
    object DrawerOpened : NavDrawerEvent()
    object DrawerClosed : NavDrawerEvent()
}