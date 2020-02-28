package com.jv.news.presenter

import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.core.state.BlockState
import com.joaquimverges.helium.navigation.drawer.NavDrawerState
import com.jv.news.data.ArticleRepository
import com.jv.news.data.SourcesRepository
import com.jv.news.presenter.state.ArticleListState

/**
 * @author joaquim
 */

class MainPresenter : LogicBlock<BlockState, BlockEvent>() {

    private val sourcesRepo = SourcesRepository()
    private val articleRepo = ArticleRepository(sourcesRepo)

    internal val articlePresenter = ArticleListPresenter(articleRepo)
    internal val sourcesPresenter = SourcesLogic(sourcesRepo)

    init {
        articlePresenter.observeState().subscribe { state ->
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
