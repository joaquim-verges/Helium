package com.joaquimverges.kmp.news.android

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.viewModel
import com.joaquimverges.kmp.news.data.Article
import com.joaquimverges.kmp.news.logic.AppRouter
import com.joaquimverges.kmp.news.logic.ArticleDetailLogic
import com.joaquimverges.kmp.news.logic.ArticleListLogic
import com.joaquimvergse.helium.compose.AppBlock

@Composable
fun AppUi(state: AppRouter.Screen?) {
    Crossfade(state) { current ->
        when (current) {
            is AppRouter.Screen.ArticleList -> ArticleList()
            is AppRouter.Screen.ArticleDetail -> ArticleDetail(current.article)
        }
    }
}

@Composable
fun ArticleList() {
    val logic: ArticleListLogic = viewModel()
    AppBlock(logic) { state, dispatcher ->
        ArticleListUI(state, dispatcher)
    }
}

@Composable
fun ArticleDetail(article: Article) {
    val logic = remember(article) { ArticleDetailLogic(article) }
    AppBlock(logic) { state, dispatcher ->
        ArticleDetailUi(state, dispatcher)
    }
}
