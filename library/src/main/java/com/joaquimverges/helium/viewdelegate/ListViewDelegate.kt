package com.joaquimverges.helium.viewdelegate

import android.support.annotation.LayoutRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.view.isVisible
import com.joaquimverges.helium.R
import com.joaquimverges.helium.event.ViewEvent
import com.joaquimverges.helium.state.NetworkViewState
import io.reactivex.subjects.PublishSubject

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
 *
 * @see com.joaquimverges.helium.presenter.ListPresenter
 * @see com.joaquimverges.helium.state.NetworkViewState
 */
class ListViewDelegate<T, E : ViewEvent, VH : BaseRecyclerViewItem<T, E>>
constructor(inflater: LayoutInflater,
            recyclerItemFactory: (LayoutInflater, ViewGroup) -> VH,
            // optional layout properties
            @LayoutRes layoutResId : Int = R.layout.view_list,
            container: ViewGroup? = null,
            addToContainer: Boolean = false,
            // optional list config
            layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(inflater.context),
            recyclerViewConfig: ((RecyclerView) -> Unit)? = null,
            emptyView: View? = null)
    : BaseViewDelegate<NetworkViewState<List<T>>, E>(layoutResId, inflater, container, addToContainer) {

    private val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
    private val progressBar: ProgressBar = view.findViewById(R.id.loader)
    private val emptyViewContainer: ViewGroup = view.findViewById(R.id.empty_view_container)
    private val adapter: BaseRecyclerAdapter<T, E, VH> = BaseRecyclerAdapter(inflater, observer() as PublishSubject<E>, recyclerItemFactory)

    init {
        recyclerView.layoutManager = layoutManager
        recyclerViewConfig?.invoke(recyclerView)
        recyclerView.adapter = adapter
        emptyView?.let { emptyViewContainer.addView(emptyView) }
    }

    override fun render(viewState: NetworkViewState<List<T>>) {
        progressBar.isVisible = false
        emptyViewContainer.isVisible = false
        when (viewState) {
            is NetworkViewState.Loading -> progressBar.isVisible = adapter.itemCount == 0
            is NetworkViewState.DataReady -> adapter.setItems(viewState.data)
            is NetworkViewState.Empty -> {
                adapter.setItems(emptyList())
                emptyViewContainer.isVisible = true
            }
        }
    }
}
