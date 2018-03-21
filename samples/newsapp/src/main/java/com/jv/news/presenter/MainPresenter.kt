package com.jv.news.presenter

import com.joaquimverges.helium.event.ViewEvent
import com.joaquimverges.helium.presenter.BasePresenter
import com.joaquimverges.helium.state.ViewState
import com.joaquimverges.helium.viewdelegate.BaseViewDelegate
import com.jv.news.data.ArticleRepository
import com.jv.news.data.SourcesRepository
import com.jv.news.view.DrawerViewDelegate

/**
 * @author joaquim
 */

class MainPresenter : BasePresenter<ViewState, ViewEvent>() {

    private val sourcesRepo = SourcesRepository()
    private val articleRepo = ArticleRepository(sourcesRepo)
    private val articlePresenter = ArticleListPresenter(articleRepo)
    private val sourcesPresenter = SourcesPresenter(sourcesRepo)

    override fun attach(viewDelegate: BaseViewDelegate<ViewState, ViewEvent>) {
        super.attach(viewDelegate)
        (viewDelegate as? DrawerViewDelegate)?.run {
            articlePresenter.attach(mainViewDelegate)
            sourcesPresenter.attach(drawerViewDelegate)
        }
    }

    override fun onViewEvent(event: ViewEvent) {
        // no-op for now
    }
}
