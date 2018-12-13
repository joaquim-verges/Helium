package com.jv.news.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.joaquimverges.helium.core.event.ViewEvent
import com.joaquimverges.helium.core.state.ViewState
import com.joaquimverges.helium.core.viewdelegate.BaseViewDelegate
import com.joaquimverges.helium.navigation.state.NavDrawerState
import com.joaquimverges.helium.navigation.viewdelegate.NavDrawerViewDelegate
import com.joaquimverges.helium.navigation.viewdelegate.ToolbarViewDelegate
import com.jv.news.R

/**
 * @author joaquim
 */
class MainViewDelegate(
    inflater: LayoutInflater,
    val toolbarView: ToolbarViewDelegate = ToolbarViewDelegate(inflater) {
        it.setDisplayHomeAsUpEnabled(true)
        it.setHomeAsUpIndicator(R.drawable.ic_menu)
    },
    val mainView: ArticleListViewDelegate = ArticleListViewDelegate(inflater),
    val drawerView: SourcesViewDelegate = SourcesViewDelegate(inflater),
    private val navDrawer: NavDrawerViewDelegate = NavDrawerViewDelegate(mainView, drawerView)
) : BaseViewDelegate<ViewState, ViewEvent>(R.layout.activity_main, inflater) {

    private val toolbarContainer = findView<ViewGroup>(R.id.toolbar_container)
    private val mainContainer = findView<ViewGroup>(R.id.main_container)

    init {
        toolbarContainer.addView(toolbarView.view)
        mainContainer.addView(navDrawer.view)
    }

    override fun render(viewState: ViewState) {
        when (viewState) {
            is NavDrawerState -> navDrawer.render(viewState)
        }
    }
}