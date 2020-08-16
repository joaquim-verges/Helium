package com.joaquimverges.kmp.news

import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.BlockState
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch

class CommonListLogic(
        private val repo: NewsRepository = NewsRepository()
) : LogicBlock<CommonListLogic.State, BlockEvent>() {

    class State(val data: String) : BlockState

    init {
        // TODO use Main in base LogicBLock
        coroutineScope.launch(Main) {
            val news = withContext(Background) {
                repo.getNews()
            }
            pushState(State(news))
        }
    }

    override fun onUiEvent(event: BlockEvent) {

    }
}