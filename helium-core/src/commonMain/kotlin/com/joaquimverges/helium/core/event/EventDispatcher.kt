package com.joaquimverges.helium.core.event

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

class EventDispatcher<E> {
    private val eventFlow: BroadcastChannel<E> = BroadcastChannel(Channel.BUFFERED)

    fun observer(): Flow<E> {
        return eventFlow.asFlow()
    }

    fun pushEvent(event: E) {
        eventFlow.offer(event)
    }
}
