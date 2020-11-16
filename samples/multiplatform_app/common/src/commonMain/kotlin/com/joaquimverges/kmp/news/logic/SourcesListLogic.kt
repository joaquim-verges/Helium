package com.joaquimverges.kmp.news.logic

import com.joaquimverges.helium.core.Background
import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.DataLoadState
import com.joaquimverges.kmp.news.data.SourceWithSelection
import com.joaquimverges.kmp.news.data.SourcesRepository
import com.joaquimverges.kmp.news.data.models.ArticleSource
import kotlinx.coroutines.withContext

class SourcesListLogic(
    private val appRouter: AppRouter,
    private val repo: SourcesRepository = SourcesRepository()
) : LogicBlock<DataLoadState<SourceWithSelection>, SourcesListLogic.Event>() {

    sealed class Event : BlockEvent {
        data class SourceSelected(val source: ArticleSource) : Event()
        data class SourceUnselected(val source: ArticleSource) : Event()
        object CloseClicked : Event()
    }

    init {
        pushState(DataLoadState.Loading())
        launchInBlock {
            try {
                val data = withContext(Background) {
                    repo.getSources()
                }
                if (data.sources.isNotEmpty()) {
                    pushState(DataLoadState.Ready(data))
                } else {
                    pushState(DataLoadState.Empty())
                }
            } catch (e: Exception) {
                pushState(DataLoadState.Error(e))
            }
        }
    }

    override fun onUiEvent(event: Event) {
        when (event) {
            is Event.SourceSelected -> repo.setSelectedSource(event.source, true)
            is Event.SourceUnselected -> repo.setSelectedSource(event.source, false)
            is Event.CloseClicked -> appRouter.goToList()
        }
    }
}
