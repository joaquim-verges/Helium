package com.joaquimverges.kmp.news.logic

import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.BlockState
import com.joaquimverges.kmp.news.BrowserWrapper
import com.joaquimverges.kmp.news.data.models.Article

class AppRouter(
    private val browserWrapper: BrowserWrapper
) : LogicBlock<AppRouter.Screen, BlockEvent>() {

    sealed class Screen : BlockState {
        object SourcesList : Screen()
        object ArticleList : Screen()
        data class ArticleDetail(val article: Article) : Screen()
    }

    init {
        // start on article list
        pushState(Screen.ArticleList)
    }

    override fun onUiEvent(event: BlockEvent) {
        // no-op
    }

    fun onBackPressed(): Boolean {
        return when (currentState()) {
            is Screen.ArticleDetail, Screen.SourcesList -> {
                pushState(Screen.ArticleList)
                true
            }
            Screen.ArticleList -> {
                false
            }
            null -> false
        }
    }

    fun goToList() {
        pushState(Screen.ArticleList)
    }

    fun goToDetail(article: Article) {
        pushState(Screen.ArticleDetail(article))
    }

    fun goToSources() {
        pushState(Screen.SourcesList)
    }

    fun goToBrowser(url: String) {
        browserWrapper.openBrowser(url)
    }
}
