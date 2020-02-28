package com.jv.news.logic

import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.core.state.BlockState
import com.joaquimverges.helium.navigation.drawer.NavDrawerState
import com.jv.news.data.ArticleRepository
import com.jv.news.data.SourcesRepository
import com.jv.news.logic.state.ArticleListState

/**
 * @author joaquim
 */

class MainScreenLogic : LogicBlock<BlockState, BlockEvent>() {

    private val sourcesRepo = SourcesRepository()
    private val articleRepo = ArticleRepository(sourcesRepo)

    internal val articleListLogic = ArticleListLogic(articleRepo)
    internal val sourcesLogic = SourcesLogic(sourcesRepo)

    init {
        articleListLogic.observeState().subscribe { state ->
            when (state) {
                ArticleListState.ArticlesLoaded -> pushState(NavDrawerState.Closed)
                ArticleListState.MoreSourcesRequested -> pushState(NavDrawerState.Opened)
            }
        }.autoDispose()
    }

    override fun onUiEvent(event: BlockEvent) {
        // no-op for now
    }
}
