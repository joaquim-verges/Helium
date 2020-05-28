package com.jv.news

import androidx.fragment.app.FragmentActivity
import com.joaquimverges.helium.core.AppBlock
import com.joaquimverges.helium.core.plus
import com.joaquimverges.helium.core.retained.getRetainedLogicBlock
import com.joaquimverges.helium.navigation.drawer.NavDrawerEvent
import com.joaquimverges.helium.navigation.drawer.NavDrawerState
import com.jv.news.logic.ArticleListLogic
import com.jv.news.logic.MainScreenLogic
import com.jv.news.ui.ArticleListUi
import com.jv.news.ui.MainScreenUi

/**
 * @author joaqu
 */
object MainAppBlock {
    fun build(activity: FragmentActivity): AppBlock<NavDrawerState, NavDrawerEvent> {
        val logic = activity.getRetainedLogicBlock<MainScreenLogic>()
        val ui = MainScreenUi(activity.layoutInflater)
        activity.setContentView(ui.view)
        return (logic + ui).withChildBlocks(
            ArticleListAppBlock.build(logic.articleListLogic, ui.articleListUi),
            logic.sourcesLogic + ui.drawerUi
        )
    }
}

object ArticleListAppBlock {
    fun build(logic: ArticleListLogic, ui: ArticleListUi) =
        (logic + ui).withChildBlocks(
            logic.toolbarLogic + ui.toolbarUi,
            logic.listLogic + ui.listUi
        )
}