package com.jv.news.ui.event

import android.content.Context
import com.joaquimverges.helium.core.event.BlockEvent
import com.jv.news.data.model.Article

/**
 * @author joaquim
 */
sealed class ArticleEvent : BlockEvent {
    data class Clicked(val context: Context, val article: Article) : ArticleEvent()
    data class LongPressed(val context: Context, val article: Article) : ArticleEvent()
    object GetMoreSources : ArticleEvent()
}
