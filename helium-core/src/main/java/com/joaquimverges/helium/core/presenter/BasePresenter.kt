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

    /**
     * Implement this method to react to any ViewEvent emissions from the attached ViewDelegate.
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
    open fun attach(viewDelegate: BaseViewDelegate<S, E>) {
        val lifecycle: Lifecycle = viewDelegate.lifecycle
                ?: throw IllegalArgumentException("Cannot attach view delegates that don't have a lifecycle aware context")
        observer().autoDispose(lifecycle).subscribe { viewDelegate.render(it) }
        viewDelegate.observer().autoDispose(lifecycle).subscribe { onViewEvent(it) }
        lifecycle.addObserver(this)
    }

    /**
     * Observe the ViewState changes from this Presenter
     */
    fun observer(): Observable<S> = viewState

    /**
     * Pushes a new state, which will trigger any active subscribers
     */
    fun pushState(state: S) = viewState.onNext(state)

    fun Disposable.autoDispose() = disposables.add(this)

    override fun onCleared() = disposables.clear()
}