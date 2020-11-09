package com.joaquimverges.kmp.news.logic

import com.joaquimverges.helium.core.Background
import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.event.EventDispatcher
import com.joaquimverges.helium.core.state.DataLoadState
import com.joaquimverges.kmp.news.data.Article
import com.joaquimverges.kmp.news.data.ArticleResponse
import com.joaquimverges.kmp.news.data.NewsRepository
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.withContext

class ArticleListLogic(
    private val appRouter: AppRouter,
    private val repo: NewsRepository = NewsRepository()
) : LogicBlock<DataLoadState<ArticleResponse>, ArticleListLogic.Event>() {

    sealed class Event : BlockEvent {
        data class ArticleClicked(val article: Article) : Event()
        object FetchMore : Event()
    }

    sealed class PaginationEvent {
        data class FirstPageLoaded(val data: ArticleResponse) : PaginationEvent()
        data class AdditionalPageLoaded(val data: ArticleResponse) : PaginationEvent()
    }

    private val paginationEvents = EventDispatcher<PaginationEvent>()

    init {
        paginationEvents.observer().scan<PaginationEvent, DataLoadState<ArticleResponse>>(
            DataLoadState.Init(),
            { prevState, paginationEvent ->
                when (paginationEvent) {
                    is PaginationEvent.FirstPageLoaded -> {
                        DataLoadState.Ready(paginationEvent.data)
                    }
                    is PaginationEvent.AdditionalPageLoaded -> {
                        when (prevState) {
                            is DataLoadState.Ready -> {
                                DataLoadState.Ready(
                                    ArticleResponse(
                                        paginationEvent.data.status,
                                        prevState.data.articles + paginationEvent.data.articles
                                    )
                                )
                            }
                            else -> prevState
                        }
                    }
                }
            }
        ).onEach { state ->
            pushState(state)
        }.launchInBlock()

        fetchFirstPage()
    }

    private fun fetchFirstPage() {
        pushState(DataLoadState.Loading())
        launchInBlock {
            try {
                val news = withContext(Background) {
                    repo.getNews()
                }
                if (news.articles.isNotEmpty()) {
                    paginationEvents.pushEvent(PaginationEvent.FirstPageLoaded(news))
                } else {
                    pushState(DataLoadState.Empty())
                }
            } catch (e: Exception) {
                pushState(DataLoadState.Error(e))
            }
        }
    }

    private fun paginate() {
        launchInBlock {
            try {
                val data = withContext(Background) {
                    repo.paginate()
                }
                if (data.articles.isNotEmpty()) {
                    paginationEvents.pushEvent(PaginationEvent.AdditionalPageLoaded(data))
                }
            } catch (error: Exception) {
                pushState(DataLoadState.Error(error))
            }
        }
    }

    override fun onUiEvent(event: Event) {
        when (event) {
            is Event.ArticleClicked -> appRouter.goToDetail(event.article)
            Event.FetchMore -> paginate()
        }
    }
}
