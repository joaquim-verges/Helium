package com.joaquimverges.helium.core

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.BlockState
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

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
abstract class LogicBlock<S : BlockState, E : BlockEvent> : ViewModel(), LifecycleObserver {

    private val disposables: CompositeDisposable = CompositeDisposable()
    private val state: BehaviorSubject<S> = BehaviorSubject.create()
    private val eventDispatcher: PublishSubject<E> = PublishSubject.create()

    /**
     * Implement this method to react to any BlockEvent emissions from the attached UiBlock.
     * UI events can also be observed externally via [observeEvents]
     */
    abstract fun onUiEvent(event: E)

    /**
     * Convenience method to create an AppBlock by assembling this logic with the given uiBlock
     */
    fun attach(uiBlock: UiBlock<S, E>) {
        AppBlock(this, uiBlock).assemble()
    }

    /**
     * Convenience method to Forward all [BlockEvent] received by this LogicBlock to another given LogicBlock
     * Must have compatible [BlockEvent] for both blocks.
     */
    fun propagateEventsTo(otherBlock: LogicBlock<*, E>) {
        eventDispatcher.subscribe { otherBlock.processEvent(it) }.autoDispose()
    }

    /**
     * Observe state changes from this LogicBlock
     */
    fun observeState(): Observable<S> = state

    /**
     * Observe events received by this LogicBlock, useful for propagating events to parent LogicBlocks
     */
    fun observeEvents(): Observable<E> = eventDispatcher

    /**
     * Pushes a new state, which will trigger any active subscribers
     */
    fun pushState(state: S) = this.state.onNext(state)

    /**
     * Will automatically dispose this subscription when the block gets cleared
     */
    fun Disposable.autoDispose() = disposables.add(this)

    override fun onCleared() = disposables.clear()

    // internal functions

    internal fun processEvent(event: E) {
        onUiEvent(event)
        eventDispatcher.onNext(event)
    }
}