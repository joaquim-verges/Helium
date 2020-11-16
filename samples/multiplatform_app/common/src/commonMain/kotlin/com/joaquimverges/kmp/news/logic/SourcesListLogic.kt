package com.joaquimverges.kmp.news.logic

import com.joaquimverges.helium.core.Background
import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.DataLoadState
import com.joaquimverges.kmp.news.data.SourcesRepository
import com.joaquimverges.kmp.news.data.models.ArticleSource
import kotlinx.coroutines.withContext

class SourcesListLogic(
    private val appRouter: AppRouter,
    private val repo: SourcesRepository = SourcesRepository()
) : LogicBlock<DataLoadState<List<ArticleSource>>, SourcesListLogic.Event>() {

    sealed class Event : BlockEvent {
        data class SourceClicked(val source: ArticleSource): Event()
        object CloseClicked: Event()
    }

    init {
        pushState(DataLoadState.Loading())
        launchInBlock {
            try {
                val sources = withContext(Background) {
                    repo.getSources()
                }
                if (sources.isNotEmpty()) {
                    pushState(DataLoadState.Ready(sources))
                } else {
                    pushState(DataLoadState.Empty())
                }
            } catch (e: Exception) {
                pushState(DataLoadState.Error(e))
            }
        }
    }

    override fun onUiEvent(event: Event) {
        when(event) {
            is Event.SourceClicked -> {
                // TODO selection logic
            }
            Event.CloseClicked -> appRouter.goToList()
        }
    }
}
