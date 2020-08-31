package com.jv.news.ui

import android.content.res.Configuration
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.joaquimverges.helium.core.UiBlock
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.BlockState
import com.joaquimverges.helium.navigation.toolbar.CollapsingToolbarUi
import com.joaquimverges.helium.ui.list.ListUi
import com.jv.news.App.Companion.context
import com.jv.news.R
import com.jv.news.data.model.Article
import com.jv.news.logic.state.ArticleListState
import com.jv.news.ui.adapter.ArticleGridItem
import com.jv.news.ui.event.ArticleEvent

/**
 * @author joaquim
 */
class ArticleListUi(
    inflater: LayoutInflater,
    val listUi: ListUi<Article, ArticleEvent, ArticleGridItem> = inflateListView(inflater)
) : CollapsingToolbarUi<ArticleListState, ArticleEvent>(
    inflater,
    listUi,
    HeaderUi(inflater),
    collapsingLayoutCustomization = {
        it.title = context.getString(R.string.app_name)
        it.setExpandedTitleTypeface(Typeface.DEFAULT_BOLD)
        it.expandedTitleMarginStart = context.resources.getDimensionPixelSize(R.dimen.expanded_toolbar_title_margin)
        // hidden in landscape
        it.visibility = when (context.resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> View.GONE
            else -> View.VISIBLE
        }
    },
    actionBarCustomization = {
        it.setDisplayHomeAsUpEnabled(true)
        it.setHomeAsUpIndicator(R.drawable.ic_menu)
    }
) {

    companion object {
        private const val DOUBLE_SPAN_COUNT = 2
        private const val SINGLE_SPAN_COUNT = 1

        private fun inflateListView(inflater: LayoutInflater): ListUi<Article, ArticleEvent, ArticleGridItem> {
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

            return ListUi(
                inflater,
                recyclerItemFactory = { layoutInflater, container ->
                    ArticleGridItem(layoutInflater, container)
                },
                layoutManager = layoutManager,
                recyclerViewConfig = { recyclerView ->
                    recyclerView.addItemDecoration(SpacesItemDecoration(spacing, orientation))
                },
                emptyUiBlock = EmptyUi(inflater),
                swipeToRefreshEnabled = true
            )
        }
    }

    override fun render(viewState: ArticleListState) {
        // no-op
    }

    class HeaderUi(inflater: LayoutInflater) : UiBlock<BlockState, BlockEvent>(R.layout.view_toolbar_backdrop, inflater) {
        override fun render(blockState: BlockState) {
            // no-op
        }
    }

    class EmptyUi(inflater: LayoutInflater) : UiBlock<BlockState, ArticleEvent>(R.layout.empty_view, inflater) {

        private val openDrawerButton = findView<View>(R.id.open_drawer_button)

        init {
            openDrawerButton.setOnClickListener { pushEvent(ArticleEvent.GetMoreSources) }
        }

        override fun render(blockState: BlockState) {
            // no-op
        }
    }
}
