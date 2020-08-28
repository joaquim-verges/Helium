package com.joaquimverges.helium.core

import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.event.EventDispatcher
import com.joaquimverges.helium.core.state.BlockState
import com.joaquimverges.helium.core.state.StateObserver
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

    private val state: StateObserver<S> = StateObserver()
    private val eventDispatcher: EventDispatcher<E> = EventDispatcher()

    /**
     * Implement this method to react to any BlockEvent emissions from the attached UiBlock.
     * UI events can also be observed externally via [observeEvents]
     */
    abstract fun onUiEvent(event: E)

    /**
     * Observe state changes from this LogicBlock
     */
    fun observeState(): Flow<S> = state.observer

    /**
     * Get the current state of this block
     */
    fun currentState(): S? = state.currentState

    /**
     * Observe events received by this LogicBlock, useful for propagating events to parent LogicBlocks
     */
    fun observeEvents(): Flow<E> = eventDispatcher.observer()

    /**
     * Pushes a new state, which will trigger any active subscribers
     */
    fun pushState(state: S) {
        this.state.pushState(state)
    }

    // internal functions

    fun processEvent(event: E) {
        onUiEvent(event)
        eventDispatcher.pushEvent(event)
    }

    fun <T> Flow<T>.launchInBlock() = launchIn(coroutineScope)

    inline fun launchInBlock(crossinline codeBlock: suspend () -> Unit) = coroutineScope.launch { codeBlock() }
}