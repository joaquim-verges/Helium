package com.joaquimverges.helium.core.event

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Abstraction for a reactive observer for one-off events
 */
class EventDispatcher<E> {
    private val eventFlow: MutableSharedFlow<E> = MutableSharedFlow(extraBufferCapacity = 64)

    fun observer(): Flow<E> {
        return eventFlow.asSharedFlow()
    }

    fun pushEvent(event: E) {
        eventFlow.tryEmit(event)
    }
}
