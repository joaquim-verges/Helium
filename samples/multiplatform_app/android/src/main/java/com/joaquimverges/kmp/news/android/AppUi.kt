package com.joaquimverges.kmp.news.android

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.ambientOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ContextAmbient
import com.joaquimverges.helium.compose.AppBlock
import com.joaquimverges.helium.core.retained.getRetainedLogicBlock
import com.joaquimverges.kmp.news.android.utils.StackTransition
import com.joaquimverges.kmp.news.data.models.Article
import com.joaquimverges.kmp.news.logic.AppRouter
import com.joaquimverges.kmp.news.logic.ArticleDetailLogic
import com.joaquimverges.kmp.news.logic.ArticleListLogic
import com.joaquimverges.kmp.news.logic.SourcesListLogic

val AppRouterAmbient = ambientOf<AppRouter> { error("No AppRouter set!") }

@Composable
fun AppUi(appRouter: AppRouter) {
    MaterialTheme(
        colors = Themes.dark
    ) {
        Providers(AppRouterAmbient provides appRouter) {
            AppBlock(appRouter) { state, _ ->
                StackTransition(
                    state,
                    shouldReverseAnimation = state == AppRouter.Screen.ArticleList
                ) { current ->
                    when (current) {
                        is AppRouter.Screen.ArticleList -> ArticleList()
                        is AppRouter.Screen.ArticleDetail -> ArticleDetail(current.article)
                        is AppRouter.Screen.SourcesList -> SourcesList()
                    }
                }
            }
        }
    }
}

@Composable
fun ArticleList() {
    val appRouter = AppRouterAmbient.current
    val logic: ArticleListLogic = ContextAmbient.current.getRetainedLogicBlock {
        ArticleListLogic(appRouter)
    }
    AppBlock(logic) { state, dispatcher ->
        ArticleListUI(state, dispatcher)
    }
}

@Composable
fun ArticleDetail(article: Article) {
    val appRouter = AppRouterAmbient.current
    val logic = remember(article) { ArticleDetailLogic(article, appRouter) }
    AppBlock(logic) { state, dispatcher ->
        ArticleDetailUi(state, dispatcher)
    }
}

@Composable
fun SourcesList() {
    val appRouter = AppRouterAmbient.current
    val logic: SourcesListLogic = ContextAmbient.current.getRetainedLogicBlock {
        SourcesListLogic(appRouter)
    }
    AppBlock(logic) { state, dispatcher ->
        SourcesListUi(state, dispatcher)
    }
}
