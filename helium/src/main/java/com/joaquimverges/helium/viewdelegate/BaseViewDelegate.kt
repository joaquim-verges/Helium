package com.joaquimverges.helium.viewdelegate

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joaquimverges.helium.event.ViewEvent
import com.joaquimverges.helium.state.ViewState
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Base class for ViewDelegates.
 * - Responsible for accessing and holding Android views
 * - Renders ViewState when attached to a Presenter
 * - Emits ViewEvent objects to the Presenter (clicks, animation ends, etc...)
 *
 * @param view the root view of the layout
 * @param viewEventsObservable the Observable that will receive the view events, like user clicks.
 *
 * @see com.joaquimverges.helium.event.ViewEvent
 * @see com.joaquimverges.helium.presenter.BasePresenter
 */
abstract class BaseViewDelegate<in S : ViewState, E : ViewEvent>
(val view: View, private val viewEventsObservable: PublishSubject<E> = PublishSubject.create()) {

    /**
     * Convenience constructor that inflates the layout for you.
     *
     * @param layoutResId the id of the layout to inflate
     * @param inflater a valid LayoutInflater
     * @param container optional container to inflate the view with
     * @param addToContainer optional flag to also add the inflated layout to the passed container
     */
    constructor(@LayoutRes layoutResId: Int,
                inflater: LayoutInflater,
                container: ViewGroup? = null,
                addToContainer: Boolean = false,
                view: View = inflater.inflate(layoutResId, container, addToContainer)) : this(view)

    protected val context: Context = view.context
    internal val lifecycle: Lifecycle? = (context as? LifecycleOwner)?.lifecycle

    /**
     * Implement this method to render a layout according to the latest pushed ViewState
     */
    abstract fun render(viewState: S)

    /**
     * Observe the ViewEvent changes from this ViewDelegate
     */
    fun observer(): Observable<E> = viewEventsObservable

    /**
     * Pushes a new ViewEvent, which will trigger active subscribers
     */
    fun pushEvent(event: E) = viewEventsObservable.onNext(event)
}
