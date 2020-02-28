package com.jv.news

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.joaquimverges.helium.core.AppBlock
import com.joaquimverges.helium.core.plus
import com.joaquimverges.helium.core.retained.getRetainedLogicBlock
import com.jv.news.presenter.ArticleListPresenter
import com.jv.news.presenter.MainPresenter
import com.jv.news.util.VersionUtil
import com.jv.news.view.ArticleListUi
import com.jv.news.view.MainViewDelegate

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLightStatusBar()

        val logic = getRetainedLogicBlock<MainPresenter>()
        val ui = MainViewDelegate(layoutInflater).also {
            setContentView(it.view)
        }
        MainBlock.create(logic, ui).assemble()
    }

    private fun setLightStatusBar() {
        if (VersionUtil.isAtLeastApi(Build.VERSION_CODES.M)) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = ContextCompat.getColor(App.context, R.color.colorPrimary)
        }
    }

    object MainBlock {
        fun create(logic: MainPresenter, ui: MainViewDelegate) = AppBlock(
            logic, ui,
            listOf(
                ArticleListBlock.create(logic.articlePresenter, ui.articleView),
                logic.sourcesPresenter + ui.drawerView
            )
        )
    }

    object ArticleListBlock {
        fun create(logic: ArticleListPresenter, ui: ArticleListUi) = AppBlock(
            logic, ui,
            listOf(
                logic.toolbarLogic + ui.toolbarViewDelegate,
                logic.listLogic + ui.listUi
            )
        )
    }
}
