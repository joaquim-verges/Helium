package com.jv.news.logic

import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.navigation.drawer.NavDrawerEvent
import com.joaquimverges.helium.navigation.drawer.NavDrawerState
import com.jv.news.data.ArticleRepository
import com.jv.news.data.SourcesRepository
import com.jv.news.logic.state.ArticleListState
import kotlinx.coroutines.flow.onEach

/**
 * @author joaquim
 */

class MainScreenLogic : LogicBlock<NavDrawerState, NavDrawerEvent>() {

    private val sourcesRepo = SourcesRepository()
    private val articleRepo = ArticleRepository(sourcesRepo)

    internal val articleListLogic = ArticleListLogic(articleRepo)
    internal val sourcesLogic = SourcesLogic(sourcesRepo)

    init {
        articleListLogic.observeState().onEach { state ->
            when (state) {
                ArticleListState.ArticlesLoaded -> pushState(NavDrawerState.Closed)
                ArticleListState.MoreSourcesRequested -> pushState(NavDrawerState.Opened)
            }
        }.launchInBlock()
    }

    override fun onUiEvent(event: NavDrawerEvent) {
        when (event) {
            NavDrawerEvent.DrawerOpened -> {
            }
            NavDrawerEvent.DrawerClosed -> articleListLogic.pushState(ArticleListState.ArticlesLoaded)
        }
    }
}
