package com.jv.news.presenter

import com.joaquimverges.helium.core.presenter.BasePresenter
import com.joaquimverges.helium.core.viewdelegate.BaseViewDelegate
import com.joaquimverges.helium.navigation.event.NavDrawerEvent
import com.joaquimverges.helium.navigation.state.NavDrawerState
import com.jv.news.data.ArticleRepository
import com.jv.news.data.SourcesRepository
import com.jv.news.presenter.state.ArticleListState
import com.jv.news.view.MainViewDelegate

/**
 * @author joaquim
 */

class MainPresenter : BasePresenter<NavDrawerState, NavDrawerEvent>() {

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

    override fun onAttached(viewDelegate: BaseViewDelegate<NavDrawerState, NavDrawerEvent>) {
        (viewDelegate as MainViewDelegate).run {
            articlePresenter.attach(mainView)
            sourcesPresenter.attach(drawerView)
        }
    }

    override fun onViewEvent(event: NavDrawerEvent) {
        // no-op for now
    }
}
