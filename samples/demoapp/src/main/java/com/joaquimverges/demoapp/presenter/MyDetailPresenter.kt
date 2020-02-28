package com.joaquimverges.demoapp.presenter

import com.joaquimverges.demoapp.data.MyDetailRepository
import com.joaquimverges.demoapp.data.MyItem
import com.joaquimverges.helium.core.event.ClickEvent
import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.ui.state.ListBlockState
import com.joaquimverges.helium.core.util.async

/**
 * @author joaquim
 */
class MyDetailPresenter(private val repository: MyDetailRepository = MyDetailRepository())
    : LogicBlock<ListBlockState<MyItem>, ClickEvent<MyItem>>() {

    init {
        loadDetailModel()
    }

    private fun loadDetailModel() {
        repository
                .getData()
                .async()
                .doOnSubscribe { pushState(ListBlockState.Loading()) }
                .subscribe(
                        { item -> pushState(ListBlockState.DataReady(item)) },
                        { error -> pushState(ListBlockState.Error(error)) }
                )
    }

    override fun onUiEvent(event: ClickEvent<MyItem>) {
        // loads a new color, publishing any state changes
        loadDetailModel()
    }
}