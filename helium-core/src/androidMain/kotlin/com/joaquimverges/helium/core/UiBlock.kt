package com.joaquimverges.helium.core

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.event.EventDispatcher
import com.joaquimverges.helium.core.state.BlockState
import kotlinx.coroutines.flow.Flow

/**
 * Base class for UiBlocks.
 * - Responsible for accessing and holding Android views
 * - Renders BlockState when attached to a LogicBLock
 * - Emits BlockEvent objects to the LogicBlock (clicks, animation ends, etc...)
 *
 * @param view the root view of the layout
 * @param eventsObservable the Observable that will receive the UI events, like user clicks.
 *
 * @see com.joaquimverges.helium.core.event.BlockEvent
 * @see com.joaquimverges.helium.core.LogicBlock
 */
abstract class UiBlock<in S : BlockState, E : BlockEvent> constructor(
        val view: View,
        private val eventDispatcher: EventDispatcher<E> = EventDispatcher(),
        protected val context: Context = view.context
) : IUiBlock<S, E> {

    /**
     * Convenience constructor that inflates the layout for you.
     *
     * @param layoutResId the id of the layout to inflate
     * @param inflater a valid LayoutInflater
     * @param container optional container to inflate the view with
     * @param addToContainer optional flag to also add the inflated layout to the passed container
     */
    constructor(
            @LayoutRes layoutResId: Int,
            inflater: LayoutInflater,
            container: ViewGroup? = null,
            addToContainer: Boolean = false,
            view: View = inflater.inflate(layoutResId, container, addToContainer)
    ) : this(view)

    /**
     * Convenience method to find a view by id within this UiBlock
     */
    protected fun <V : View> findView(@IdRes resId: Int): V = view.findViewById(resId)

    /**
     * Implement this method to render a layout according to the latest pushed ViewState
     */
    abstract override fun render(state: S)

    /**
     * Observe the events pushed from this UiBlock
     */
    override fun observer(): Flow<E> = eventDispatcher.observer()

    /**
     * Pushes a new BlockEvent, which will trigger active subscribers LogicBlocks
     */
    override fun pushEvent(event: E) {
        eventDispatcher.pushEvent(event)
    }
}
