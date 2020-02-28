package com.jv.news.ui

import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joaquimverges.helium.core.UiBlock
import com.joaquimverges.helium.ui.list.event.ListBlockEvent
import com.joaquimverges.helium.ui.list.state.DataLoadState
import com.jv.news.R
import com.jv.news.data.model.ArticleSource
import com.jv.news.data.model.SourcesCategoryGroup
import com.jv.news.ui.adapter.ExpandableSourcesAdapter
import com.jv.news.ui.event.SourceEvent

/**
 * @author joaquim
 */
class SourcesUi internal constructor(inflater: LayoutInflater) :
    UiBlock<DataLoadState<List<SourcesCategoryGroup>>, ListBlockEvent<SourceEvent>>(R.layout.view_sources_list, inflater) {

    private val layoutManager: LinearLayoutManager = LinearLayoutManager(context)
    private val recyclerView: RecyclerView = findView((R.id.sources_list))
    private val progressBar: ProgressBar = findView((R.id.loader))

    init {
        recyclerView.layoutManager = layoutManager
    }

    override fun render(viewState: DataLoadState<List<SourcesCategoryGroup>>) {
        progressBar.visibility = View.GONE
        when (viewState) {
            is DataLoadState.Loading -> progressBar.visibility = if (recyclerView.adapter?.itemCount == 0) View.VISIBLE else View.GONE
            is DataLoadState.Ready -> recyclerView.adapter = createAdapter(viewState.data)
        }
    }

    private fun createAdapter(groups: List<SourcesCategoryGroup>): ExpandableSourcesAdapter {
        return ExpandableSourcesAdapter(context, groups).apply {
            setChildClickListener { view, checked, group, childIndex ->
                if (checked) {
                    pushEvent(ListBlockEvent.ListItemEvent(SourceEvent.Selected(view.context, group.items[childIndex] as ArticleSource)))
                } else {
                    pushEvent(ListBlockEvent.ListItemEvent(SourceEvent.Unselected(view.context, group.items[childIndex] as ArticleSource)))
                }
            }
        }
    }
}
