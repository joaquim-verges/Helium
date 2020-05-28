package com.joaquimverges.helium.ui.list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.joaquimverges.helium.core.event.BlockEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel

/**
 * Base class for RecyclerView items.
 * - Responsible for accessing and holding Android views.
 * - Can bind a model to an Android view within the list
 * - Can emit BlockEvent objects propagated up to a LogicBlock (clicks for example)
 *
 * @param view The root of the layout for this list item
 */
abstract class ListItem<in T, V : BlockEvent>(val view: View) : RecyclerView.ViewHolder(view) {

    /**
     * Convenience constructor to inflate the layout for you.
     *
     * @param layoutResId the id of the layout to inflate
     * @param inflater a valid LayoutInflater
     * @param container mandatory container to inflate the view with
     */
    constructor(
        @LayoutRes layoutResId: Int,
        inflater: LayoutInflater,
        container: ViewGroup,
        view: View = inflater.inflate(layoutResId, container, false)
    ) : this(view)

    internal var viewEvents: BroadcastChannel<V>? = null
    protected var context: Context = itemView.context

    abstract fun bind(data: T)

    fun pushEvent(event: V) = viewEvents?.offer(event)

    /**
     * Convenience method to find a view by id within this recyclerViewItem
     */
    protected fun <V : View> findView(@IdRes resId: Int): V = itemView.findViewById(resId)
}