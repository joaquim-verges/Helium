package com.jv.news.view.event

import android.content.Context
import com.joaquimverges.helium.core.event.BlockEvent
import com.jv.news.data.model.ArticleSource

/**
 * @author joaquim
 */
sealed class SourceEvent : BlockEvent {
    data class Selected(val context: Context, val source: ArticleSource) : SourceEvent()
    data class Unselected(val context: Context, val source: ArticleSource) : SourceEvent()
}