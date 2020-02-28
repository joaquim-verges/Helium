package com.joaquimverges.helium.navigation.toolbar

import com.joaquimverges.helium.core.event.BlockEvent

/**
 * User events coming from a [ToolbarUi]
 */
sealed class ToolbarEvent : BlockEvent {
    object HomeClicked : ToolbarEvent()
    data class MenuItemClicked(val id: Int) : ToolbarEvent()
}