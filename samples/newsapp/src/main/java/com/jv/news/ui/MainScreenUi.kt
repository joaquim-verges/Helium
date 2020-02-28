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
    internal val articleView: ArticleListUi = ArticleListUi(inflater),
    internal val drawerView: SourcesUi = SourcesUi(inflater),
    private val navDrawer: NavDrawerUi = NavDrawerUi(
        articleView,
        drawerView
    )
) : UiBlock<BlockState, BlockEvent>(R.layout.activity_main, inflater) {

    private val mainContainer = findView<ViewGroup>(R.id.main_container)

    init {
        mainContainer.addView(navDrawer.view)
    }

    override fun render(blockState: BlockState) {
        when (blockState) {
            is NavDrawerState -> navDrawer.render(blockState)
        }
    }
}