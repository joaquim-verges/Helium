package com.jv.news.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.BlockState
import com.joaquimverges.helium.core.UiBlock
import com.joaquimverges.helium.navigation.state.NavDrawerState
import com.joaquimverges.helium.navigation.viewdelegate.NavDrawerViewDelegate
import com.jv.news.R

/**
 * @author joaquim
 */
class MainViewDelegate(
    inflater: LayoutInflater,
    internal val articleView: ArticleListViewDelegate = ArticleListViewDelegate(inflater),
    internal val drawerView: SourcesViewDelegate = SourcesViewDelegate(inflater),
    private val navDrawer: NavDrawerViewDelegate = NavDrawerViewDelegate(articleView, drawerView)
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