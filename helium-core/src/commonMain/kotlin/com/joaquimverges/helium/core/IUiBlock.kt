package com.joaquimverges.helium.core

import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.BlockState
import kotlinx.coroutines.flow.Flow

/**
 * @author joaqu
 */
interface IUiBlock<in S : BlockState, E : BlockEvent> {
    /**
     * Implement this method to render a layout according to the latest pushed ViewState
     */
    fun render(state: S)

    /**
     * Observe the events pushed from this UiBlock
     */
    fun observer(): Flow<E>

    /**
     * Pushes a new BlockEvent, which will trigger active subscribers LogicBlocks
     */
    fun pushEvent(event: E)
}