package com.joaquimverges.helium.navigation.bottomnav

import android.content.Context
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.joaquimverges.helium.core.event.BlockEvent

/**
 * Events coming from a [BottomNavUi]
 */
sealed class BottomNavEvent : BlockEvent {
    data class NavigationChanged(val context: Context, val controller: NavController, val destination: NavDestination, val arguments: Bundle?) : BottomNavEvent()
    data class NavItemReselected(val context: Context, @IdRes val menuId: Int): BottomNavEvent()
}