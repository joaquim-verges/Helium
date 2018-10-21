package com.jv.news.view.event

import android.content.Context
import com.joaquimverges.helium.core.event.ViewEvent
import com.jv.news.data.model.Article

/**
 * @author joaquim
 */
sealed class ArticleEvent : ViewEvent {
    data class Clicked(val context: Context, val article: Article) : ArticleEvent()
    data class LongPressed(val context: Context, val article: Article) : ArticleEvent()
    object GetMoreSources : ArticleEvent()
}