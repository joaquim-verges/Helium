package com.jv.news.presenter

import com.joaquimverges.helium.core.event.ViewEvent
import com.joaquimverges.helium.core.presenter.BasePresenter
import com.joaquimverges.helium.core.state.ViewState
import com.joaquimverges.helium.core.viewdelegate.BaseViewDelegate
import com.joaquimverges.helium.navigation.state.NavDrawerState
import com.jv.news.data.ArticleRepository
import com.jv.news.data.SourcesRepository
import com.jv.news.presenter.state.ArticleListState
import com.jv.news.view.MainViewDelegate

/**
 * @author joaquim
 */

class MainPresenter : BasePresenter<ViewState, ViewEvent>() {

    private val sourcesRepo = SourcesRepository()
    private val articleRepo = ArticleRepository(sourcesRepo)

    private val articlePresenter = ArticleListPresenter(articleRepo)
    private val sourcesPresenter = SourcesPresenter(sourcesRepo)

    init {
        articlePresenter.stateObserver().subscribe { state ->
            when (state) {
                ArticleListState.ArticlesLoaded -> pushState(NavDrawerState.Closed)
                ArticleListState.MoreSourcesRequested -> pushState(NavDrawerState.Opened)
            }
        }.autoDispose()
    }

    override fun onAttached(viewDelegate: BaseViewDelegate<ViewState, ViewEvent>) {
        (viewDelegate as MainViewDelegate).run {
            articlePresenter.attach(mainView)
            sourcesPresenter.attach(drawerView)
        }
    }

    override fun onViewEvent(event: ViewEvent) {
        // no-op for now
    }
}
