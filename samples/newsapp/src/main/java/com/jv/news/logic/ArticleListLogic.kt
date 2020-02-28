package com.jv.news.logic

import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ShareCompat
import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.navigation.toolbar.ToolbarEvent
import com.joaquimverges.helium.navigation.toolbar.ToolbarLogic
import com.joaquimverges.helium.ui.list.event.ListBlockEvent
import com.joaquimverges.helium.ui.list.ListLogic
import com.joaquimverges.helium.ui.util.RefreshPolicy
import com.jv.news.data.ArticleRepository
import com.jv.news.data.model.Article
import com.jv.news.logic.state.ArticleListState
import com.jv.news.ui.event.ArticleEvent
import java.util.concurrent.TimeUnit

/**
 * @author: joaquim
 */
class ArticleListLogic(
    private val repository: ArticleRepository,
    refreshPolicy: RefreshPolicy = RefreshPolicy(10, TimeUnit.MINUTES),
    internal val listLogic: ListLogic<Article, ArticleEvent> = ListLogic(
        repository,
        refreshPolicy
    ),
    internal val toolbarLogic: ToolbarLogic = ToolbarLogic()
) : LogicBlock<ArticleListState, ArticleEvent>() {

    init {
        repository
            .sourcesUpdatedObserver()
            .subscribe { listLogic.loadFirstPage() }
            .autoDispose()

        // receive all list item view events in this block
        listLogic.observeEvents().subscribe {
            when (it) {
                is ListBlockEvent.ListItemEvent -> onUiEvent(it.itemEvent)
                is ListBlockEvent.EmptyBlockEvent -> onUiEvent(it.emptyViewEvent)
                is ListBlockEvent.UserScrolledBottom -> listLogic.paginate()
                is ListBlockEvent.SwipedToRefresh -> listLogic.loadFirstPage()
            }
        }.autoDispose()
        // when the list changes state, propagate the state up to the MainPresenter
        // so it can close the nav drawer after 2s
        listLogic.observeState()
            .debounce(2, TimeUnit.SECONDS)
            .subscribe { pushState(ArticleListState.ArticlesLoaded) }
            .autoDispose()

        toolbarLogic.observeEvents().subscribe {
            when (it) {
                is ToolbarEvent.HomeClicked -> pushState(ArticleListState.MoreSourcesRequested)
            }
        }.autoDispose()
    }

    override fun onUiEvent(event: ArticleEvent) {
        when (event) {
            is ArticleEvent.Clicked -> openArticle(event.context, event.article)
            is ArticleEvent.LongPressed -> shareArticle(event.context, event.article)
            is ArticleEvent.GetMoreSources -> pushState(ArticleListState.MoreSourcesRequested)
        }
    }

    private fun shareArticle(context: Context, article: Article) {
        context.startActivity(
            ShareCompat.IntentBuilder
                .from(context as Activity)
                .setType("text/plain")
                .setText("${article.title} - ${article.url}")
                .createChooserIntent()
        )
    }

    private fun openArticle(context: Context, article: Article) {
        CustomTabsIntent.Builder().build().launchUrl(context, Uri.parse(article.url))
    }
}
