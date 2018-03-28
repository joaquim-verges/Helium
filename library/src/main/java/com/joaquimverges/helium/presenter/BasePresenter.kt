package com.joaquimverges.helium.presenter

import android.arch.lifecycle.*
import com.joaquimverges.helium.viewdelegate.BaseViewDelegate
import com.joaquimverges.helium.event.ViewEvent
import com.joaquimverges.helium.state.ViewState
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

/**
 * Presenters holds and publishes ViewState changes to a ViewDelegate for rendering.
 * It also receives any published ViewEvent from the attached ViewDelegate.
 *
 * @see [com.joaquimverges.helium.viewdelegate.BaseViewDelegate]
 * @see [com.joaquimverges.helium.state.ViewState]
 * @see [com.joaquimverges.helium.event.ViewEvent]
 */
abstract class BasePresenter<S : ViewState, E: ViewEvent> : ViewModel(), LifecycleObserver {

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
        observer().bindToLifecycle(viewDelegate.view).subscribe { viewDelegate.render(it) }
        viewDelegate.observer().bindToLifecycle(viewDelegate.view).subscribe { onViewEvent(it) }
        (viewDelegate.view.context as? LifecycleOwner)?.lifecycle?.addObserver(this)
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