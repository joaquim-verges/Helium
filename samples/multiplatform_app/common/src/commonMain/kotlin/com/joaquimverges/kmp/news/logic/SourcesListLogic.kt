package com.joaquimverges.kmp.news.logic

import com.joaquimverges.helium.core.Background
import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.DataLoadState
import com.joaquimverges.kmp.news.Sources
import com.joaquimverges.kmp.news.data.SourceWithSelection
import com.joaquimverges.kmp.news.data.SourcesRepository
import com.joaquimverges.kmp.news.data.models.ArticleSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

class SourcesListLogic(
    private val appRouter: AppRouter,
    private val repo: SourcesRepository = SourcesRepository()
) : LogicBlock<DataLoadState<List<Sources>>, SourcesListLogic.Event>() {

    sealed class Event : BlockEvent {
        data class SourceSelected(val source: Sources) : Event()
        data class SourceUnselected(val source: Sources) : Event()
        object CloseClicked : Event()
    }

    init {
        pushState(DataLoadState.Loading())
        launchInBlock {
            withContext(Background) {
                repo.refreshSources()
            }
        }
        repo.observeSources()
            .onEach {
                pushState(DataLoadState.Ready(it))
            }.catch { pushState(DataLoadState.Error(it)) }
            .launchInBlock()
    }

    override fun onUiEvent(event: Event) {
        when (event) {
            is Event.SourceSelected -> repo.setSelectedSource(event.source, true)
            is Event.SourceUnselected -> repo.setSelectedSource(event.source, false)
            is Event.CloseClicked -> appRouter.goToList()
        }
    }
}
