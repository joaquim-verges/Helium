package com.jv.news.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.joaquimverges.helium.core.UiBlock
import com.joaquimverges.helium.navigation.drawer.NavDrawerEvent
import com.joaquimverges.helium.navigation.drawer.NavDrawerState
import com.joaquimverges.helium.navigation.drawer.NavDrawerUi
import com.jv.news.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf

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
) : UiBlock<NavDrawerState, NavDrawerEvent>(R.layout.activity_main, inflater) {

    private val mainContainer = findView<ViewGroup>(R.id.main_container)

    init {
        mainContainer.addView(navDrawerUi.view)
    }

    override fun render(state: NavDrawerState) {
        navDrawerUi.render(state)
    }

    override fun observer(): Flow<NavDrawerEvent> {
        return flowOf(super.observer(), navDrawerUi.observer()).flattenMerge()
    }
}