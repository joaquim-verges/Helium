package com.joaquimverges.kmp.news.logic

import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.BlockState
import com.joaquimverges.kmp.news.data.Article

class AppRouter : LogicBlock<AppRouter.Screen, BlockEvent>() {

    companion object {
        private val instance by lazy { AppRouter() }
        fun get() = instance
    }

    sealed class Screen : BlockState {
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
        if (currentState() is Screen.ArticleDetail) {
            pushState(Screen.ArticleList)
            return true
        }
        return false
    }
}