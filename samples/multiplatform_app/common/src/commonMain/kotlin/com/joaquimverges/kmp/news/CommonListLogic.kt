package com.joaquimverges.kmp.news

import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.DataLoadState
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch

class CommonListLogic(
        private val repo: NewsRepository = NewsRepository()
) : LogicBlock<DataLoadState<ArticleResponse>, BlockEvent>() {

    init {
        pushState(DataLoadState.Loading())
        // TODO use Main in base LogicBLock
        coroutineScope.launch(Main) {
            try {
                val news = withContext(Background) {
                    repo.getNews()
                }
                pushState(DataLoadState.Ready(news))
            } catch (e: Exception) {
                pushState(DataLoadState.Error(e))
            }
        }
    }

    override fun onUiEvent(event: BlockEvent) {

    }
}