package com.jv.news

import androidx.fragment.app.FragmentActivity
import com.joaquimverges.helium.core.AppBlock
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.plus
import com.joaquimverges.helium.core.retained.getRetainedLogicBlock
import com.joaquimverges.helium.core.state.BlockState
import com.jv.news.logic.ArticleListLogic
import com.jv.news.logic.MainScreenLogic
import com.jv.news.ui.ArticleListUi
import com.jv.news.ui.MainScreenUi

/**
 * @author joaqu
 */
object MainAppBlock {
    fun build(activity: FragmentActivity): AppBlock<BlockState, BlockEvent> {
        val logic = activity.getRetainedLogicBlock<MainScreenLogic>()
        val ui = MainScreenUi(activity.layoutInflater)
        activity.setContentView(ui.view)
        return (logic + ui).withChildBlocks(
            ArticleListAppBlock.build(logic.articleListLogic, ui.articleView),
            logic.sourcesLogic + ui.drawerView
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