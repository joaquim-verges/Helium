package com.joaquimverges.helium.ui.viewdelegate

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.joaquimverges.helium.core.event.ViewEvent
import com.joaquimverges.helium.core.viewdelegate.BaseViewDelegate
import com.joaquimverges.helium.ui.R
import com.joaquimverges.helium.ui.state.ListViewState
import io.reactivex.subjects.PublishSubject
import java.util.Collections.emptyList

/**
 * A convenience ViewDelegate that displays a list of data.
 * Handles displaying the list in a RecyclerView, with a ProgressBar while loading.
 * Responds to NetworkViewState changes emitted from a Presenter.
 *
 * @param inflater LayoutInflater to inflate the view hierarchy
 * @param recyclerItemFactory Provides how to create ViewHolders for item views
 * @param layoutResId optional custom layout id. Must contain a R.id.recycler_view and R.id.loader.
 * @param container optional ViewGroup to inflate this layout with
 * @param container whether the layout should be added to the passed container
 * @param layoutManager optional custom layoutManager. Default is LinearLayoutManager.
 * @param recyclerViewConfig optional hook to configure the recyclerView with custom item decorators, touch handlers, scroll listeners, etc.
 * @param emptyViewDelegate optional view delegate to show when the list adapter is empty
 *
 * @see com.joaquimverges.helium.ui.presenter.ListPresenter
 * @see com.joaquimverges.helium.ui.state.ListViewState
 */
open class ListViewDelegate<T, E : ViewEvent, VH : BaseRecyclerViewItem<T, E>>
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
    emptyViewDelegate: BaseViewDelegate<*, E>? = null
) : BaseViewDelegate<ListViewState<List<T>>, E>(layoutResId, inflater, container, addToContainer) {

    private val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
    private val progressBar: ProgressBar = view.findViewById(R.id.loader)
    private val emptyViewContainer: ViewGroup = view.findViewById(R.id.empty_view_container)
    private val adapter: BaseRecyclerAdapter<T, E, VH> = BaseRecyclerAdapter(inflater, observer() as PublishSubject<E>, recyclerItemFactory)

    init {
        recyclerView.layoutManager = layoutManager
        recyclerViewConfig?.invoke(recyclerView)
        recyclerView.adapter = adapter
        emptyViewDelegate?.let {
            it.observer().subscribe { event: E -> pushEvent(event) }
            emptyViewContainer.addView(it.view)
        }
    }

    override fun render(viewState: ListViewState<List<T>>) {
        progressBar.setVisible(false)
        emptyViewContainer.setVisible(false)
        when (viewState) {
            is ListViewState.Loading -> progressBar.setVisible(adapter.itemCount == 0)
            is ListViewState.DataReady -> adapter.setItems(viewState.data)
            is ListViewState.Empty -> {
                adapter.setItems(emptyList())
                emptyViewContainer.setVisible(true)
            }
        }
    }

    private fun View.setVisible(value: Boolean) {
        visibility = if (value) View.VISIBLE else View.GONE
    }
}
