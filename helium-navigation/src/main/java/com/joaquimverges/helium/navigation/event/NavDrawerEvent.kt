package com.joaquimverges.helium.navigation.event

import com.joaquimverges.helium.core.event.ViewEvent

/**
 * @author joaqu
 */
sealed class NavDrawerEvent : ViewEvent {
    object DrawerOpened : NavDrawerEvent()
    object DrawerClosed : NavDrawerEvent()
}