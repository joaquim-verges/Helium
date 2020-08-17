package com.joaquimverges.kmp.news

import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.DataLoadState
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch

class CommonListLogic(
        private val repo: NewsRepository = NewsRepository()
) : LogicBlock<DataLoadState<String>, BlockEvent>() {

    init {
        pushState(DataLoadState.Loading())
        // TODO use Main in base LogicBLock
        coroutineScope.launch(Main) {
            val news = withContext(Background) {
                repo.getNews()
            }
            pushState(DataLoadState.Ready(news))
        }
    }

    override fun onUiEvent(event: BlockEvent) {

    }
}