package com.joaquimverges.demoapp.logic

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.joaquimverges.demoapp.data.MyDetailRepository
import com.joaquimverges.demoapp.data.MyItem
import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.core.event.ClickEvent
import com.joaquimverges.helium.core.state.DataLoadState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author joaquim
 */
class MyDetailLogic(
    private val repository: MyDetailRepository = MyDetailRepository()
) : LogicBlock<DataLoadState<MyItem>, ClickEvent<MyItem>>() {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun loadDetailModel() {
        launchInBlock {
            try {
                pushState(DataLoadState.Loading())
                val data = withContext(Dispatchers.IO) {
                    repository.getData()
                }
                pushState(DataLoadState.Ready(data))
            } catch (error: Exception) {
                pushState(DataLoadState.Error(error))
            }
        }
    }

    override fun onUiEvent(event: ClickEvent<MyItem>) {
        // loads a new color, publishing any state changes
        loadDetailModel()
    }
}