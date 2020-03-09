package com.joaquimverges.demoapp.logic

import com.joaquimverges.demoapp.data.MyDetailRepository
import com.joaquimverges.demoapp.data.MyItem
import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.core.event.ClickEvent
import com.joaquimverges.helium.core.util.async
import com.joaquimverges.helium.core.state.DataLoadState

/**
 * @author joaquim
 */
class MyDetailLogic(
    private val repository: MyDetailRepository = MyDetailRepository()
) : LogicBlock<DataLoadState<MyItem>, ClickEvent<MyItem>>() {

    init {
        loadDetailModel()
    }

    private fun loadDetailModel() {
        repository
            .getFirstPage()
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