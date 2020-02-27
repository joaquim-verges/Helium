package com.jv.news.presenter

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ShareCompat
import com.joaquimverges.helium.core.presenter.BasePresenter
import com.joaquimverges.helium.core.viewdelegate.BaseViewDelegate
import com.joaquimverges.helium.navigation.event.ToolbarEvent
import com.joaquimverges.helium.navigation.presenter.ToolbarPresenter
import com.joaquimverges.helium.ui.event.ListViewEvent
import com.joaquimverges.helium.ui.presenter.ListPresenter
import com.joaquimverges.helium.ui.state.ListViewState
import com.joaquimverges.helium.ui.util.RefreshPolicy
import com.jv.news.data.ArticleRepository
import com.jv.news.data.model.Article
import com.jv.news.presenter.state.ArticleListState
import com.jv.news.view.ArticleListViewDelegate
import com.jv.news.view.event.ArticleEvent
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * @author: joaquim
 */
class ArticleListPresenter(
    private val repository: ArticleRepository,
    refreshPolicy: RefreshPolicy = RefreshPolicy(10, TimeUnit.MINUTES),
    internal val listPresenter: ListPresenter<Article, ArticleEvent> = ListPresenter(repository, refreshPolicy),
    internal val toolbarPresenter: ToolbarPresenter = ToolbarPresenter()
) : BasePresenter<ArticleListState, ArticleEvent>() {

    init {
        repository
            .sourcesUpdatedObserver()
            .subscribe { listPresenter.loadData() }
            .autoDispose()

        // receive all list item view events in this presenter
        listPresenter.observeViewEvents().subscribe {
            when (it) {
                is ListViewEvent.ListItemEvent -> onViewEvent(it.itemEvent)
                is ListViewEvent.EmptyViewEvent -> onViewEvent(it.emptyViewEvent)
                is ListViewEvent.UserScrolledBottom -> listPresenter.paginate()
                is ListViewEvent.SwipedToRefresh -> listPresenter.loadData()
            }
        }.autoDispose()
        // when the list changes state, propagate the state up to the MainPresenter
        // so it can close the nav drawer after 2s
        listPresenter.observeViewState()
            .debounce(2, TimeUnit.SECONDS)
            .subscribe { pushState(ArticleListState.ArticlesLoaded) }
            .autoDispose()

        toolbarPresenter.observeViewEvents().subscribe {
            when (it) {
                is ToolbarEvent.HomeClicked -> pushState(ArticleListState.MoreSourcesRequested)
            }
        }.autoDispose()
    }

    override fun onViewEvent(event: ArticleEvent) {
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
