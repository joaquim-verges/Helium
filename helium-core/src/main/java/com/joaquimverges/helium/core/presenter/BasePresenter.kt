package com.joaquimverges.helium.core.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.ViewModel
import com.joaquimverges.helium.core.event.ViewEvent
import com.joaquimverges.helium.core.state.ViewState
import com.joaquimverges.helium.core.util.autoDispose
import com.joaquimverges.helium.core.viewdelegate.BaseViewDelegate
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/**
 * Presenters holds and publishes ViewState changes to a ViewDelegate for rendering.
 * It also receives any published ViewEvent from the attached ViewDelegate.
 *
 * @see [com.joaquimverges.helium.core.viewdelegate.BaseViewDelegate]
 * @see [com.joaquimverges.helium.core.state.ViewState]
 * @see [com.joaquimverges.helium.core.event.ViewEvent]
 */
abstract class BasePresenter<S : ViewState, E : ViewEvent> : ViewModel(), LifecycleObserver {

    private val disposables: CompositeDisposable = CompositeDisposable()
    private val viewState: BehaviorSubject<S> = BehaviorSubject.create()
    private val viewEventDispatcher: PublishSubject<E> = PublishSubject.create()

    /**
     * Implement this method to react to any ViewEvent emissions from the attached ViewDelegate.
     * View events can also be observed externally via [observeViewEvents]
     */
    abstract fun onViewEvent(event: E)

    /**
     * This is where the binding happens:
     * - viewDelegate subscribes to state updates
     * - presenter subscribes to view events
     * - presenter gets access to lifecycle events
     *
     * Any method can be annotated with [@OnLifecycleEvent(Lifecycle.Event.ON_RESUME)]
     * or any other [Lifecycle.Event] and will be called at the appropriate time.
     */
    fun attach(viewDelegate: BaseViewDelegate<S, E>) {
        val lifecycle: Lifecycle = viewDelegate.lifecycle
            ?: throw IllegalArgumentException("Cannot attach view delegates that don't have a lifecycle aware context")
        observeViewState().autoDispose(lifecycle).subscribe { viewDelegate.render(it) }
        viewDelegate.observer().autoDispose(lifecycle).subscribe { processViewEvent(it) }
        lifecycle.addObserver(this)
        onAttached(viewDelegate)
    }

    /**
     * Called when this presenter has successfully been attached to a ViewDelegate and its lifecycle
     * This is as a good time to attach sub presenters to sub view delegates
     */
    open fun onAttached(viewDelegate: BaseViewDelegate<S, E>) {
        // override to attach sub presenters
    }

    /**
     * Convenience method to Forward all [ViewEvent] received by this presenter to the given presenter
     * Must have compatible [ViewEvent] for both presenters
     */
    fun propagateViewEventsTo(otherPresenter: BasePresenter<*, E>) {
        viewEventDispatcher.subscribe { otherPresenter.processViewEvent(it) }.autoDispose()
    }

    /**
     * Observe ViewState changes from this Presenter
     */
    fun observeViewState(): Observable<S> = viewState

    /**
     * Observe ViewEvents received by this Presenter, useful for propagating events to other presenters
     */
    fun observeViewEvents(): Observable<E> = viewEventDispatcher

    /**
     * Pushes a new state, which will trigger any active subscribers
     */
    fun pushState(state: S) = viewState.onNext(state)

    /**
     * Will automatically dispose this subscription when the presenter gets cleared
     */
    fun Disposable.autoDispose() = disposables.add(this)

    override fun onCleared() = disposables.clear()

    // internal functions

    private fun processViewEvent(event: E) {
        onViewEvent(event)
        viewEventDispatcher.onNext(event)
    }
}