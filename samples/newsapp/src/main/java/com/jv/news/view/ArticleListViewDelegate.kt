package com.jv.news.view

import android.content.res.Configuration
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.joaquimverges.helium.core.event.ViewEvent
import com.joaquimverges.helium.core.state.ViewState
import com.joaquimverges.helium.core.viewdelegate.BaseViewDelegate
import com.joaquimverges.helium.navigation.viewdelegate.CollapsingToolbarScreenViewDelegate
import com.joaquimverges.helium.ui.viewdelegate.ListViewDelegate
import com.jv.news.R
import com.jv.news.data.model.Article
import com.jv.news.presenter.state.ArticleListState
import com.jv.news.view.adapter.ArticleGridItemViewHolder
import com.jv.news.view.event.ArticleEvent

/**
 * @author joaquim
 */
class ArticleListViewDelegate(
    inflater: LayoutInflater
) : BaseViewDelegate<ArticleListState, ArticleEvent>(
    R.layout.view_article_list,
    inflater
) {

    companion object {
        private const val DOUBLE_SPAN_COUNT = 2
        private const val SINGLE_SPAN_COUNT = 1
    }

    class HeaderViewDelegate(inflater: LayoutInflater) : BaseViewDelegate<ViewState, ViewEvent>(R.layout.view_toolbar_backdrop, inflater) {
        override fun render(viewState: ViewState) {

        }
    }

    val listViewDelegate = inflateListView(inflater)
    private val container = findView<ViewGroup>(R.id.article_list_container)
    private val collapsingToolbarScreenViewDelegate = CollapsingToolbarScreenViewDelegate(
        inflater,
        listViewDelegate,
        HeaderViewDelegate(inflater),
        collapsingLayoutCustomization = {
            it.title = "Helium News"
            it.setExpandedTitleTypeface(Typeface.DEFAULT_BOLD)
            it.expandedTitleMarginStart = context.resources.getDimensionPixelSize(R.dimen.expanded_toolbar_title_margin)
        },
        actionBarCustomization = {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
        },
        toolbarCustomization = {
            it.visibility = when (context.resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> View.GONE
                else -> View.VISIBLE
            }
        }
    )
    val toolbarViewDelegate = collapsingToolbarScreenViewDelegate.toolbarViewDelegate

    init {
        container.addView(collapsingToolbarScreenViewDelegate.view)
    }

    override fun render(viewState: ArticleListState) {
        // no-op
    }

    private fun inflateListView(inflater: LayoutInflater): ListViewDelegate<Article, ArticleEvent, ArticleGridItemViewHolder> {
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

        return ListViewDelegate(
            inflater,
            recyclerItemFactory = { layoutInflater, container ->
                ArticleGridItemViewHolder(layoutInflater, container)
            },
            layoutManager = layoutManager,
            recyclerViewConfig = { recyclerView ->
                recyclerView.addItemDecoration(SpacesItemDecoration(spacing, orientation))
            },
            emptyViewDelegate = EmptyViewDelegate(inflater)
        )
    }

    class EmptyViewDelegate(inflater: LayoutInflater) : BaseViewDelegate<ViewState, ArticleEvent>(R.layout.empty_view, inflater) {

        private val openDrawerButton = findView<View>(R.id.open_drawer_button)

        init {
            openDrawerButton.setOnClickListener { pushEvent(ArticleEvent.GetMoreSources) }
        }

        override fun render(viewState: ViewState) {
            // no-op
        }
    }
}
