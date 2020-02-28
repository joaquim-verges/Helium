package com.joaquimverges.helium.navigation.event

import com.joaquimverges.helium.core.event.BlockEvent

/**
 * User events coming from a [NavDrawerViewDelegate]
 */
sealed class NavDrawerEvent : BlockEvent {
    object DrawerOpened : NavDrawerEvent()
    object DrawerClosed : NavDrawerEvent()
}