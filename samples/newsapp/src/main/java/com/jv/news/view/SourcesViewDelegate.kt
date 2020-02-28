package com.jv.news.view

import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joaquimverges.helium.core.BaseViewDelegate
import com.joaquimverges.helium.ui.event.ListViewEvent
import com.joaquimverges.helium.ui.state.ListViewState
import com.jv.news.R
import com.jv.news.data.model.ArticleSource
import com.jv.news.data.model.SourcesCategoryGroup
import com.jv.news.view.adapter.ExpandableSourcesAdapter
import com.jv.news.view.event.SourceEvent

/**
 * @author joaquim
 */
class SourcesViewDelegate internal constructor(inflater: LayoutInflater) :
    BaseViewDelegate<ListViewState<List<SourcesCategoryGroup>>, ListViewEvent<SourceEvent>>(R.layout.view_sources_list, inflater) {

    private val layoutManager: LinearLayoutManager = LinearLayoutManager(context)
    private val recyclerView: RecyclerView = findView((R.id.sources_list))
    private val progressBar: ProgressBar = findView((R.id.loader))

    init {
        recyclerView.layoutManager = layoutManager
    }

    override fun render(viewState: ListViewState<List<SourcesCategoryGroup>>) {
        progressBar.visibility = View.GONE
        when (viewState) {
            is ListViewState.Loading -> progressBar.visibility = if (recyclerView.adapter?.itemCount == 0) View.VISIBLE else View.GONE
            is ListViewState.DataReady -> recyclerView.adapter = createAdapter(viewState.data)
        }
    }

    private fun createAdapter(groups: List<SourcesCategoryGroup>): ExpandableSourcesAdapter {
        return ExpandableSourcesAdapter(context, groups).apply {
            setChildClickListener { view, checked, group, childIndex ->
                if (checked) {
                    pushEvent(ListViewEvent.ListItemEvent(SourceEvent.Selected(view.context, group.items[childIndex] as ArticleSource)))
                } else {
                    pushEvent(ListViewEvent.ListItemEvent(SourceEvent.Unselected(view.context, group.items[childIndex] as ArticleSource)))
                }
            }
        }
    }
}
