package com.joaquimverges.helium.ui.viewdelegate

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joaquimverges.helium.core.event.ViewEvent
import io.reactivex.subjects.PublishSubject

/**
 * Base class for Recycler view items.
 * - Responsible for accessing and holding Android views.
 * - Can bind a model to an Android view within the list
 * - Can emit ViewEvent objects up to the Presenter (click for example)
 *
 * @param view The root of the layout for this list item
 * @param viewEvents the observable to push ViewEvent objects to
 */
abstract class BaseRecyclerViewItem<in T, V : ViewEvent>(val view: View) : RecyclerView.ViewHolder(view) {

    /**
     * Convenience constructor to inflate the layout for you.
     *
     * @param layoutResId the id of the layout to inflate
     * @param inflater a valid LayoutInflater
     * @param container mandatory container to inflate the view with
     */
    constructor(@LayoutRes layoutResId: Int,
                inflater: LayoutInflater,
                container: ViewGroup,
                view : View = inflater.inflate(layoutResId, container, false)) : this(view)

    internal var viewEvents: PublishSubject<V>? = null

    abstract fun bind(data: T)

    fun pushEvent(event: V) = viewEvents?.onNext(event)
}