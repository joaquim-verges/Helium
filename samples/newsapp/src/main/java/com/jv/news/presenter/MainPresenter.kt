package com.jv.news.presenter

import com.joaquimverges.helium.core.event.ViewEvent
import com.joaquimverges.helium.core.presenter.BasePresenter
import com.joaquimverges.helium.core.state.ViewState
import com.joaquimverges.helium.core.viewdelegate.BaseViewDelegate
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
