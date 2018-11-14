package com.jv.news.presenter

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.ShareCompat
import com.joaquimverges.helium.core.presenter.BasePresenter
import com.joaquimverges.helium.core.viewdelegate.BaseViewDelegate
import com.joaquimverges.helium.ui.presenter.ListPresenter
import com.joaquimverges.helium.ui.util.RefreshPolicy
import com.jv.news.data.ArticleRepository
import com.jv.news.data.model.Article
import com.jv.news.presenter.state.ArticleListState
import com.jv.news.view.ArticleListViewDelegate
import com.jv.news.view.event.ArticleEvent
import java.util.concurrent.TimeUnit

/**
 * @author: joaquim
 */
class ArticleListPresenter(
    repository: ArticleRepository = ArticleRepository(),
    refreshPolicy: RefreshPolicy = RefreshPolicy(10, TimeUnit.MINUTES),
    private val listPresenter: ListPresenter<Article, ArticleEvent> = ListPresenter(repository, refreshPolicy)
) : BasePresenter<ArticleListState, ArticleEvent>() {

    init {
        repository
            .sourcesUpdatedObserver()
            .subscribe { listPresenter.loadData() }
            .autoDispose()

        // receive all list view events in this presenter
        listPresenter.propagateViewEventsTo(this)
        // when the list changes state, propagate the state up to the MainPresenter
        // so it can close the nav drawer after 2s
        listPresenter.stateObserver()
            .debounce(2, TimeUnit.SECONDS)
            .subscribe { pushState(ArticleListState.ArticlesLoaded) }
            .autoDispose()
    }

    override fun onAttached(viewDelegate: BaseViewDelegate<ArticleListState, ArticleEvent>) {
        (viewDelegate as ArticleListViewDelegate).run {
            listPresenter.attach(listViewDelegate)
        }
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
