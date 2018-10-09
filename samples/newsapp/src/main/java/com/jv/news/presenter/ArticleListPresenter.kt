package com.jv.news.presenter

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.ShareCompat
import com.joaquimverges.helium.ui.presenter.ListPresenter
import com.joaquimverges.helium.ui.util.RefreshPolicy
import com.jv.news.data.ArticleRepository
import com.jv.news.data.model.Article
import com.jv.news.view.event.ArticleEvent
import java.util.concurrent.TimeUnit

/**
 * @author: joaquim
 */
class ArticleListPresenter(repository: ArticleRepository = ArticleRepository(),
                           refreshPolicy: RefreshPolicy = RefreshPolicy(10, TimeUnit.MINUTES))
    : ListPresenter<Article, ArticleEvent>(repository, refreshPolicy) {

    init {
        repository
                .sourcesUpdatedObserver()
                .subscribe { loadData() }
                .autoDispose()
    }

    override fun onViewEvent(event: ArticleEvent) {
        when (event) {
            is ArticleEvent.Clicked -> openArticle(event.context, event.article)
            is ArticleEvent.LongPressed -> shareArticle(event.context, event.article)
        }
    }

    private fun shareArticle(context: Context, article: Article) {
        context.startActivity(
                ShareCompat.IntentBuilder
                        .from(context as Activity)
                        .setType("text/plain")
                        .setText("${article.title} - ${article.url}").createChooserIntent()
        )
    }

    private fun openArticle(context: Context, article: Article) {
        CustomTabsIntent.Builder().build().launchUrl(context, Uri.parse(article.url))
    }
}
