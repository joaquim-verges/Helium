package com.joaquimverges.helium.core.event

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

/**
 * Abstraction for a reactive observer for one-off events
 */
class EventDispatcher<E> {
    private val eventFlow: BroadcastChannel<E> = BroadcastChannel(Channel.BUFFERED)

    fun observer(): Flow<E> {
        return eventFlow.asFlow()
    }

    fun pushEvent(event: E) {
        eventFlow.offer(event)
    }
}
