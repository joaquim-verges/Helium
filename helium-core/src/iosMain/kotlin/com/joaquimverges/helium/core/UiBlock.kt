package com.joaquimverges.helium.core

import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.event.EventDispatcher
import com.joaquimverges.helium.core.state.BlockState
import kotlinx.coroutines.flow.Flow

/**
 * UiBlock implementation for iOS
 */
class UiBlock<in S : BlockState, E : BlockEvent>(
    private val eventDispatcher: EventDispatcher<E> = EventDispatcher(),
    private val renderer: (S) -> Unit
) : IUiBlock<S, E> {

    /**
     * Delegates render calls to the passed [renderer] lambda
     */
    override fun render(state: S) {
        renderer(state)
    }

    /**
     * Observe the events pushed from this UiBlock
     */
    override fun observer(): Flow<E> {
        return eventDispatcher.observer()
    }

    /**
     * Pushes a new BlockEvent, which will trigger active subscribers LogicBlocks
     */
    override fun pushEvent(event: E) {
        eventDispatcher.pushEvent(event)
    }
}
