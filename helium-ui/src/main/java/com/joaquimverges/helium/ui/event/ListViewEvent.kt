package com.joaquimverges.helium.ui.event

import com.joaquimverges.helium.core.event.ViewEvent

/**
 * Describes all possible user events coming from a list based component
 */
sealed class ListViewEvent<E : ViewEvent> : ViewEvent {
    class UserScrolledHalfWay<E : ViewEvent> : ListViewEvent<E>()
    class UserScrolledBottom<E : ViewEvent> : ListViewEvent<E>()
    class SwipedToRefresh<E: ViewEvent> : ListViewEvent<E>()
    data class ListItemEvent<E : ViewEvent>(val itemEvent: E) : ListViewEvent<E>()
    data class EmptyViewEvent<E : ViewEvent>(val emptyViewEvent: E) : ListViewEvent<E>()
}
