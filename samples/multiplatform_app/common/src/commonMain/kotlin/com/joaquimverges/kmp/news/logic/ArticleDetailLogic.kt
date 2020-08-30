package com.joaquimverges.kmp.news.logic

import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.BlockState
import com.joaquimverges.kmp.news.data.Article

class ArticleDetailLogic(
    private val article: Article,
    private val appRouter: AppRouter = AppRouter.get()
) : LogicBlock<ArticleDetailLogic.DetailState, ArticleDetailLogic.DetailEvent>() {

    data class DetailState(
        val article: Article
    ) : BlockState

    sealed class DetailEvent : BlockEvent {
        object ArticleClosed : DetailEvent()
    }

    init {
        pushState(DetailState(article))
    }

    override fun onUiEvent(event: DetailEvent) {
        when (event) {
            DetailEvent.ArticleClosed -> appRouter.pushState(AppRouter.Screen.ArticleList)
        }
    }
}
