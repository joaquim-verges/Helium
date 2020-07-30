package com.joaquimverges.helium.core

import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.BlockState
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * A LogicBlock holds and publishes BlockState changes to a UiBlock for rendering.
 * It also receives any published BlockEvent from the attached UiBlock.
 *
 * Any method in this class can be annotated with [@OnLifecycleEvent(Lifecycle.Event.ON_RESUME)]
 * or any other [Lifecycle.Event] and will be called at the appropriate time.
 *
 * @see [com.joaquimverges.helium.core.UiBlock]
 * @see [com.joaquimverges.helium.core.state.BlockState]
 * @see [com.joaquimverges.helium.core.event.BlockEvent]
 */
abstract class LogicBlock<S : BlockState, E : BlockEvent> : HeliumViewModel() {

    private val state: MutableStateFlow<S?> = MutableStateFlow(null)
    private val eventDispatcher: BroadcastChannel<E> = BroadcastChannel(Channel.BUFFERED)

    /**
     * Implement this method to react to any BlockEvent emissions from the attached UiBlock.
     * UI events can also be observed externally via [observeEvents]
     */
    abstract fun onUiEvent(event: E)

    /**
     * Convenience method to Forward all [BlockEvent] received by this LogicBlock to another given LogicBlock
     * Must have compatible [BlockEvent] for both blocks.
     */
    fun propagateEventsTo(otherBlock: LogicBlock<*, E>) {
        eventDispatcher.asFlow().onEach { otherBlock.processEvent(it) }.launchInBlock()
    }

    /**
     * Observe state changes from this LogicBlock
     */
    fun observeState(): Flow<S> = state.filterNotNull()

    /**
     * Observe events received by this LogicBlock, useful for propagating events to parent LogicBlocks
     */
    fun observeEvents(): Flow<E> = eventDispatcher.asFlow()

    /**
     * Pushes a new state, which will trigger any active subscribers
     */
    fun pushState(state: S) {
        this.state.value = state
    }

    // internal functions

    internal fun processEvent(event: E) {
        onUiEvent(event)
        eventDispatcher.offer(event)
    }

    fun <T> Flow<T>.launchInBlock() = launchIn(coroutineScope)

    inline fun launchInBlock(crossinline codeBlock: suspend () -> Unit) = coroutineScope.launch { codeBlock() }
}