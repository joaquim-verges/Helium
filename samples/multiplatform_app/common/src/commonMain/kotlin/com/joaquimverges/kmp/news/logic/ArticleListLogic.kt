package com.joaquimverges.kmp.news.logic

import com.joaquimverges.helium.core.Background
import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.DataLoadState
import com.joaquimverges.kmp.news.data.Article
import com.joaquimverges.kmp.news.data.ArticleResponse
import com.joaquimverges.kmp.news.data.NewsRepository
import kotlinx.coroutines.withContext

class ArticleListLogic(
    private val appRouter: AppRouter,
    private val repo: NewsRepository = NewsRepository()
) : LogicBlock<DataLoadState<ArticleResponse>, ArticleListLogic.Event>() {

    sealed class Event : BlockEvent {
        data class ArticleClicked(val article: Article) : Event()
    }

    init {
        pushState(DataLoadState.Loading())
        launchInBlock {
            try {
                val news = withContext(Background) {
                    repo.getNews()
                }
                pushState(DataLoadState.Ready(news))
            } catch (e: Exception) {
                pushState(DataLoadState.Error(e))
            }
        }
    }

    override fun onUiEvent(event: Event) {
        when (event) {
            is Event.ArticleClicked -> appRouter.goToDetail(event.article)
        }
    }
}
