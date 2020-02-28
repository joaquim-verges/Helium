package com.joaquimverges.helium.ui.event

import com.joaquimverges.helium.core.event.BlockEvent

/**
 * Describes all possible user events coming from a list based component
 */
sealed class ListBlockEvent<E : BlockEvent> : BlockEvent {
    class UserScrolledHalfWay<E : BlockEvent> : ListBlockEvent<E>()
    class UserScrolledBottom<E : BlockEvent> : ListBlockEvent<E>()
    class SwipedToRefresh<E: BlockEvent> : ListBlockEvent<E>()
    data class ListItemEvent<E : BlockEvent>(val itemEvent: E) : ListBlockEvent<E>()
    data class EmptyBlockEvent<E : BlockEvent>(val emptyViewEvent: E) : ListBlockEvent<E>()
}
