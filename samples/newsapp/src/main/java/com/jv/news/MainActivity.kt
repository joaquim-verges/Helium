package com.jv.news

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.joaquimverges.helium.core.AppBlock
import com.joaquimverges.helium.core.plus
import com.joaquimverges.helium.core.retained.getRetainedLogicBlock
import com.jv.news.logic.ArticleListLogic
import com.jv.news.logic.MainScreenLogic
import com.jv.news.util.VersionUtil
import com.jv.news.ui.ArticleListUi
import com.jv.news.ui.MainScreenUi

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLightStatusBar()

        val logic = getRetainedLogicBlock<MainScreenLogic>()
        val ui = MainScreenUi(layoutInflater).also {
            setContentView(it.view)
        }
        MainAppBlock.create(logic, ui).assemble()
    }

    private fun setLightStatusBar() {
        if (VersionUtil.isAtLeastApi(Build.VERSION_CODES.M)) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = ContextCompat.getColor(App.context, R.color.colorPrimary)
        }
    }

    object MainAppBlock {
        fun create(logic: MainScreenLogic, ui: MainScreenUi) = AppBlock(
            logic, ui,
            listOf(
                ArticleListAppBlock.create(logic.articleListLogic, ui.articleView),
                logic.sourcesLogic + ui.drawerView
            )
        )
    }

    object ArticleListAppBlock {
        fun create(logic: ArticleListLogic, ui: ArticleListUi) = AppBlock(
            logic, ui,
            listOf(
                logic.toolbarLogic + ui.toolbarUi,
                logic.listLogic + ui.listUi
            )
        )
    }
}
