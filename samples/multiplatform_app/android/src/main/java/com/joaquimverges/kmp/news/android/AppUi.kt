package com.joaquimverges.kmp.news.android

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.viewModel
import com.joaquimverges.kmp.news.android.utils.StackTransition
import com.joaquimverges.kmp.news.data.Article
import com.joaquimverges.kmp.news.logic.AppRouter
import com.joaquimverges.kmp.news.logic.ArticleDetailLogic
import com.joaquimverges.kmp.news.logic.ArticleListLogic
import com.joaquimvergse.helium.compose.AppBlock

@Composable
fun AppUi(appRouter: AppRouter) {
    AppBlock(appRouter) { state, _ ->
        StackTransition(
            state,
            shouldReverseAnimation = state == AppRouter.Screen.ArticleList
        ) { current ->
            when (current) {
                is AppRouter.Screen.ArticleList -> ArticleList(appRouter)
                is AppRouter.Screen.ArticleDetail -> ArticleDetail(appRouter, current.article)
            }
        }
    }
}

@Composable
fun ArticleList(appRouter: AppRouter) {
    val logic = remember { ArticleListLogic(appRouter) }
    AppBlock(logic) { state, dispatcher ->
        ArticleListUI(state, dispatcher)
    }
}

@Composable
fun ArticleDetail(appRouter: AppRouter, article: Article) {
    val logic = remember(article) { ArticleDetailLogic(article, appRouter) }
    AppBlock(logic) { state, dispatcher ->
        ArticleDetailUi(state, dispatcher)
    }
}
