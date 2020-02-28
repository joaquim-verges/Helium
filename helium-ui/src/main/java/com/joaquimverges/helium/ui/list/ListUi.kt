package com.joaquimverges.helium.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.util.autoDispose
import com.joaquimverges.helium.core.UiBlock
import com.joaquimverges.helium.ui.R
import com.joaquimverges.helium.ui.list.event.ListBlockEvent
import com.joaquimverges.helium.ui.list.state.DataLoadState
import com.joaquimverges.helium.ui.list.adapter.ListAdapter
import com.joaquimverges.helium.ui.list.adapter.ListItem
import java.util.Collections.emptyList

/**
 * A convenience UiBlock that displays a list of data.
 * Handles displaying the list in a RecyclerView, with a ProgressBar while loading.
 * Responds to ListBlockState changes emitted from a compatible LogicBlock.
 *
 * @param inflater LayoutInflater to inflate the view hierarchy
 * @param recyclerItemFactory Provides how to create ViewHolders for item views
 * @param layoutResId optional custom layout id. Must contain a R.id.recycler_view and R.id.loader.
 * @param container optional ViewGroup to inflate this layout with
 * @param container whether the layout should be added to the passed container
 * @param layoutManager optional custom layoutManager. Default is LinearLayoutManager.
 * @param recyclerViewConfig optional hook to configure the recyclerView with custom item decorators, touch handlers, scroll listeners, etc.
 * @param emptyUiBlock optional ui block to show when the list adapter is empty
 *
 * @see com.joaquimverges.helium.ui.list.ListLogic
 * @see com.joaquimverges.helium.ui.list.state.DataLoadState
 */
open class ListUi<T, E : BlockEvent, VH : ListItem<T, E>>
constructor(
    inflater: LayoutInflater,
    recyclerItemFactory: (LayoutInflater, ViewGroup) -> VH,
    // optional layout properties
    @LayoutRes layoutResId: Int = R.layout.view_list,
    container: ViewGroup? = null,
    addToContainer: Boolean = false,
    // optional list config
    layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(inflater.context),
    recyclerViewConfig: ((RecyclerView) -> Unit)? = null,
    emptyUiBlock: UiBlock<*, E>? = null,
    swipeToRefreshEnabled: Boolean = false
) : UiBlock<DataLoadState<List<T>>, ListBlockEvent<E>>(layoutResId, inflater, container, addToContainer) {

    private val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
    private val swipeRefreshLayout: SwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
    private val progressBar: ProgressBar = view.findViewById(R.id.loader)
    private val emptyViewContainer: ViewGroup = view.findViewById(R.id.empty_view_container)
    private val adapter: ListAdapter<T, E, VH> = ListAdapter(inflater, recyclerItemFactory)
    private var lastItemCountHalfway = 0
    private var lastItemCountBottom = 0

    init {
        recyclerView.layoutManager = layoutManager
        recyclerViewConfig?.invoke(recyclerView)
        recyclerView.adapter = adapter
        // swipe to refresh
        swipeRefreshLayout.isEnabled = swipeToRefreshEnabled
        swipeRefreshLayout.setOnRefreshListener { pushEvent(ListBlockEvent.SwipedToRefresh()) }
        // scrolling
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItems = recyclerView.adapter?.itemCount ?: 0
                val firstPosition = (recyclerView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()
                    ?: (recyclerView.layoutManager as? GridLayoutManager)?.findFirstVisibleItemPosition() ?: 0
                if (firstPosition > (totalItems / 2) && lastItemCountHalfway != totalItems) {
                    lastItemCountHalfway = totalItems
                    pushEvent(ListBlockEvent.UserScrolledHalfWay())
                }
                if (firstPosition + recyclerView.childCount >= totalItems && lastItemCountBottom != totalItems) {
                    lastItemCountBottom = totalItems
                    pushEvent(ListBlockEvent.UserScrolledBottom())
                }
            }
        })
        // adapter items
        adapter.observeItemEvents().autoDispose(lifecycle).subscribe { ev -> pushEvent(ListBlockEvent.ListItemEvent(ev)) }
        // empty view
        emptyUiBlock?.let {
            it.observer().autoDispose(lifecycle).subscribe { event: E -> pushEvent(ListBlockEvent.EmptyBlockEvent(event)) }
            emptyViewContainer.addView(it.view)
        }
    }

    override fun render(viewState: DataLoadState<List<T>>) {
        progressBar.setVisible(false)
        emptyViewContainer.setVisible(false)
        val emptyAdapter = adapter.itemCount == 0
        swipeRefreshLayout.isRefreshing = (viewState is DataLoadState.Loading && !emptyAdapter)
        when (viewState) {
            is DataLoadState.Loading -> progressBar.setVisible(emptyAdapter)
            is DataLoadState.Ready -> {
                adapter.setItems(viewState.data)
                resetScrollCounts()
            }
            is DataLoadState.Empty -> {
                adapter.setItems(emptyList())
                emptyViewContainer.setVisible(true)
            }
        }
    }

    private fun resetScrollCounts() {
        lastItemCountHalfway = 0
        lastItemCountBottom = 0
    }

    private fun View.setVisible(value: Boolean) {
        visibility = if (value) View.VISIBLE else View.GONE
    }
}
