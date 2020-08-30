package com.joaquimverges.kmp.news.android

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animatedFloat
import androidx.compose.animation.core.AnimatedFloat
import androidx.compose.animation.core.AnimationEndReason
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Stack
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.viewinterop.viewModel
import com.joaquimverges.kmp.news.data.Article
import com.joaquimverges.kmp.news.logic.AppRouter
import com.joaquimverges.kmp.news.logic.ArticleDetailLogic
import com.joaquimverges.kmp.news.logic.ArticleListLogic

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
        ArticleDetailUi(state)
    }
}
