package com.joaquimverges.helium.navigation.bottomnav

import android.view.MenuItem
import com.joaquimverges.helium.core.event.BlockEvent

/**
 * Events coming from a [BottomNavUi]
 */
sealed class BottomNavEvent : BlockEvent {
    data class NavItemSelected(val item: MenuItem) : BottomNavEvent()
    data class NavItemReSelected(val item: MenuItem) : BottomNavEvent()
}