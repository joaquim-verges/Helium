package com.jv.news.view

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.joaquimverges.helium.state.NetworkViewState
import com.joaquimverges.helium.viewdelegate.BaseViewDelegate
import com.jv.news.R
import com.jv.news.data.model.ArticleSource
import com.jv.news.data.model.SourcesCategoryGroup
import com.jv.news.view.adapter.ExpandableSourcesAdapter
import com.jv.news.view.event.SourceEvent

/**
 * @author joaquim
 */
class SourcesViewDelegate internal constructor(contentView: View) : BaseViewDelegate<NetworkViewState<List<SourcesCategoryGroup>>, SourceEvent>(contentView) {

    companion object {
        fun create(inflater: LayoutInflater, parent: ViewGroup): SourcesViewDelegate {
            return SourcesViewDelegate(inflater.inflate(R.layout.view_sources_list, parent, true))
        }
    }

    private val layoutManager: LinearLayoutManager = LinearLayoutManager(context)
    private val recyclerView: RecyclerView = contentView.findViewById((R.id.sources_list))
    private val progressBar: ProgressBar = contentView.findViewById((R.id.loader))

    init {
        recyclerView.layoutManager = layoutManager
    }

    override fun render(viewState: NetworkViewState<List<SourcesCategoryGroup>>) {
        progressBar.visibility = View.GONE
        when (viewState) {
            is NetworkViewState.Loading -> progressBar.visibility = if (recyclerView.adapter?.itemCount == 0) View.VISIBLE else View.GONE
            is NetworkViewState.DataReady -> recyclerView.adapter = createAdapter(viewState.data)
        }
    }

    private fun createAdapter(groups: List<SourcesCategoryGroup>): ExpandableSourcesAdapter {
        return ExpandableSourcesAdapter(context, groups).apply {
            setChildClickListener { view, checked, group, childIndex ->
                pushEvent(if (checked) {
                    SourceEvent.Selected(view.context, group.items[childIndex] as ArticleSource)
                } else {
                    SourceEvent.Unselected(view.context, group.items[childIndex] as ArticleSource)
                })
            }
        }
    }
}
