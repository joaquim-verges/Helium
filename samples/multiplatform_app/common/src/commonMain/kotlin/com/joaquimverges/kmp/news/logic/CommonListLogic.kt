package com.joaquimverges.kmp.news.logic

import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.DataLoadState
import com.joaquimverges.kmp.news.Background
import com.joaquimverges.kmp.news.Main
import com.joaquimverges.kmp.news.data.Article
import com.joaquimverges.kmp.news.data.ArticleResponse
import com.joaquimverges.kmp.news.data.NewsRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch

class CommonListLogic(
        private val appRouter: AppRouter = AppRouter.get(),
        private val repo: NewsRepository = NewsRepository()
) : LogicBlock<DataLoadState<ArticleResponse>, CommonListLogic.Event>() {

    sealed class Event : BlockEvent {
        data class ArticleClicked(val article: Article) : Event()
    }

    init {
        pushState(DataLoadState.Loading())
        // TODO use Main in base LogicBLock
        coroutineScope.launch(Main) {
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
            is Event.ArticleClicked -> navigateToDetail(event.article)
        }
    }

    private fun navigateToDetail(article: Article) {
        appRouter.pushState(AppRouter.Screen.ArticleDetail(article))
    }
}