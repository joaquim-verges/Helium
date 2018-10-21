package com.jv.news.view

import android.content.res.Configuration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joaquimverges.helium.ui.viewdelegate.ListViewDelegate
import com.jv.news.App.Companion.context
import com.jv.news.R
import com.jv.news.data.model.Article
import com.jv.news.view.adapter.ArticleGridItemViewHolder
import com.jv.news.view.event.ArticleEvent

/**
 * @author joaquim
 */
object ArticleListViewDelegate {

    private const val DOUBLE_SPAN_COUNT = 2
    private const val SINGLE_SPAN_COUNT = 1

    fun create(inflater: LayoutInflater, emptyViewClickListener: () -> Unit): ListViewDelegate<Article, ArticleEvent, ArticleGridItemViewHolder> {
        val context = inflater.context
        val spacing: Int = context.resources.getDimensionPixelSize(R.dimen.grid_spacing)
        val orientation = when (context.resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> LinearLayoutManager.HORIZONTAL
            else -> LinearLayoutManager.VERTICAL
        }
        val layoutManager = GridLayoutManager(context, DOUBLE_SPAN_COUNT, orientation, false)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (orientation == LinearLayoutManager.HORIZONTAL) {
                    return DOUBLE_SPAN_COUNT
                }
                return if (position % 5 == 0) DOUBLE_SPAN_COUNT else SINGLE_SPAN_COUNT
            }
        }
        val emptyView = inflater.inflate(R.layout.empty_view, null, false)
        emptyView.findViewById<View>(R.id.open_drawer_button).setOnClickListener { emptyViewClickListener.invoke() }

        return ListViewDelegate(
                inflater,
                recyclerItemFactory = { layoutInflater, container ->
                    ArticleGridItemViewHolder(layoutInflater, container)
                },
                layoutManager = layoutManager,
                recyclerViewConfig = { recyclerView ->
                    recyclerView.addItemDecoration(SpacesItemDecoration(spacing, orientation))
                },
                emptyView = emptyView
        )
    }
}
