package com.jv.news.presenter

import com.joaquimverges.helium.core.event.ViewEvent
import com.joaquimverges.helium.core.presenter.BasePresenter
import com.joaquimverges.helium.core.state.ViewState
import com.joaquimverges.helium.navigation.state.NavDrawerState
import com.jv.news.data.ArticleRepository
import com.jv.news.data.SourcesRepository
import com.jv.news.presenter.state.ArticleListState

/**
 * @author joaquim
 */

class MainPresenter : BasePresenter<ViewState, ViewEvent>() {

    private val sourcesRepo = SourcesRepository()
    private val articleRepo = ArticleRepository(sourcesRepo)

    internal val articlePresenter = ArticleListPresenter(articleRepo)
    internal val sourcesPresenter = SourcesPresenter(sourcesRepo)

    init {
        articlePresenter.observeViewState().subscribe { state ->
            when (state) {
                ArticleListState.ArticlesLoaded -> pushState(NavDrawerState.Closed)
                ArticleListState.MoreSourcesRequested -> pushState(NavDrawerState.Opened)
            }
        }.autoDispose()
    }

    override fun onViewEvent(event: ViewEvent) {
        // no-op for now
    }
}
