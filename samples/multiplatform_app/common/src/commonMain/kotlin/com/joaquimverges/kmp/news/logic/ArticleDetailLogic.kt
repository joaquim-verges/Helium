package com.joaquimverges.kmp.news.logic

import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.BlockState
import com.joaquimverges.kmp.news.data.Article

class ArticleDetailLogic(
        private val article: Article
) : LogicBlock<ArticleDetailLogic.DetailState, BlockEvent>() {

    data class DetailState(
            val article: Article
    ) : BlockState

    init {
        pushState(DetailState(article))
    }

    override fun onUiEvent(event: BlockEvent) {
        // no-op
    }

}