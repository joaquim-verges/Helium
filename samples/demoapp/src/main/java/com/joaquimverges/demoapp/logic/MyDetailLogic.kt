package com.joaquimverges.demoapp.logic

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.joaquimverges.demoapp.data.MyDetailRepository
import com.joaquimverges.demoapp.data.MyItem
import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.core.event.ClickEvent
import com.joaquimverges.helium.core.state.DataLoadState
import com.joaquimverges.helium.core.util.async

/**
 * @author joaquim
 */
class MyDetailLogic(
    private val repository: MyDetailRepository = MyDetailRepository()
) : LogicBlock<DataLoadState<MyItem>, ClickEvent<MyItem>>() {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun loadDetailModel() {
        repository
            .getData()
            .async()
            .doOnSubscribe { pushState(DataLoadState.Loading()) }
            .subscribe(
                { item -> pushState(DataLoadState.Ready(item)) },
                { error -> pushState(DataLoadState.Error(error)) }
            ).autoDispose()
    }

    override fun onUiEvent(event: ClickEvent<MyItem>) {
        // loads a new color, publishing any state changes
        loadDetailModel()
    }
}