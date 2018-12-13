package com.joaquimverges.helium.navigation.event

import com.joaquimverges.helium.core.event.ViewEvent

/**
 * User events coming from a [ToolbarViewDelegate]
 */
sealed class ToolbarEvent : ViewEvent {
    object HomeClicked : ToolbarEvent()
    data class MenuItemClicked(val id: Int) : ToolbarEvent()
}