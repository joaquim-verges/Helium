package com.jv.news.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.BlockState
import com.joaquimverges.helium.core.UiBlock
import com.joaquimverges.helium.navigation.drawer.NavDrawerState
import com.joaquimverges.helium.navigation.drawer.NavDrawerUi
import com.jv.news.R

/**
 * @author joaquim
 */
class MainScreenUi(
    inflater: LayoutInflater,
    internal val articleListUi: ArticleListUi = ArticleListUi(inflater),
    internal val drawerUi: SourcesUi = SourcesUi(inflater),
    private val navDrawerUi: NavDrawerUi = NavDrawerUi(
        articleListUi,
        drawerUi
    )
) : UiBlock<BlockState, BlockEvent>(R.layout.activity_main, inflater) {

    private val mainContainer = findView<ViewGroup>(R.id.main_container)

    init {
        mainContainer.addView(navDrawerUi.view)
    }

    override fun render(state: BlockState) {
        when (state) {
            is NavDrawerState -> navDrawerUi.render(state)
        }
    }
}