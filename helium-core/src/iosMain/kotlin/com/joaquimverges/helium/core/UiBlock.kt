package com.joaquimverges.helium.core

import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.BlockState
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

/**
 * @author joaqu
 */
class UiBlock<in S : BlockState, E : BlockEvent>(
        private val eventDispatcher: EventDispatcher<E> = EventDispatcher(),
        private val renderer : (S) -> Unit
) : IUiBlock<S, E> {

    /**
     * Implement this method to render a layout according to the latest pushed ViewState
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