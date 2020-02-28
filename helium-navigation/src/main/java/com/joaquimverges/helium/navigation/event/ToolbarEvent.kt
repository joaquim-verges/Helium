package com.joaquimverges.helium.navigation.event

import com.joaquimverges.helium.core.event.BlockEvent

/**
 * User events coming from a [ToolbarViewDelegate]
 */
sealed class ToolbarEvent : BlockEvent {
    object HomeClicked : ToolbarEvent()
    data class MenuItemClicked(val id: Int) : ToolbarEvent()
}