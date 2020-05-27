package com.joaquimverges.demoapp.logic

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.joaquimverges.demoapp.data.MyDetailRepository
import com.joaquimverges.demoapp.data.MyItem
import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.core.event.ClickEvent
import com.joaquimverges.helium.core.state.DataLoadState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

/**
 * @author joaquim
 */
class MyDetailLogic(
    private val repository: MyDetailRepository = MyDetailRepository()
) : LogicBlock<DataLoadState<MyItem>, ClickEvent<MyItem>>() {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun loadDetailModel() {
        flow { emit(repository.getData()) }
            .flowOn(Dispatchers.IO)
            .onStart { pushState(DataLoadState.Loading()) }
            .catch { error -> pushState(DataLoadState.Error(error)) }
            .onEach { item -> pushState(DataLoadState.Ready(item)) }
            .launchInBlock()
    }

    override fun onUiEvent(event: ClickEvent<MyItem>) {
        // loads a new color, publishing any state changes
        loadDetailModel()
    }
}