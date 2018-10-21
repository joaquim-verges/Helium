package com.jv.news.presenter

import com.joaquimverges.helium.core.presenter.BasePresenter
import com.joaquimverges.helium.navigation.event.NavDrawerEvent
import com.joaquimverges.helium.navigation.state.NavDrawerState
import com.jv.news.data.ArticleRepository
import com.jv.news.data.SourcesRepository

/**
 * @author joaquim
 */

class MainPresenter : BasePresenter<NavDrawerState, NavDrawerEvent>() {

    private val sourcesRepo = SourcesRepository()
    private val articleRepo = ArticleRepository(sourcesRepo)

    val articlePresenter = ArticleListPresenter(articleRepo)
    val sourcesPresenter = SourcesPresenter(sourcesRepo)

    override fun onViewEvent(event: NavDrawerEvent) {
        // no-op for now
    }
}
