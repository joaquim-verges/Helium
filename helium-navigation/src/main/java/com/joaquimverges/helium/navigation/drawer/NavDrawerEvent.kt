package com.joaquimverges.helium.navigation.drawer

import com.joaquimverges.helium.core.event.BlockEvent

/**
 * User events coming from a [NavDrawerViewDelegate]
 */
sealed class NavDrawerEvent : BlockEvent {
    object DrawerOpened : NavDrawerEvent()
    object DrawerClosed : NavDrawerEvent()
}