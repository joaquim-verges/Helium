package com.joaquimverges.demoapp.presenter

import com.joaquimverges.demoapp.data.MyDetailRepository
import com.joaquimverges.demoapp.data.MyItem
import com.joaquimverges.helium.core.event.ClickEvent
import com.joaquimverges.helium.core.presenter.BasePresenter
import com.joaquimverges.helium.ui.state.ListViewState
import com.joaquimverges.helium.core.util.async

/**
 * @author joaquim
 */
class MyDetailPresenter(private val repository: MyDetailRepository = MyDetailRepository())
    : BasePresenter<ListViewState<MyItem>, ClickEvent<MyItem>>() {

    init {
        loadDetailModel()
    }

    private fun loadDetailModel() {
        repository
                .getData()
                .async()
                .doOnSubscribe { pushState(ListViewState.Loading()) }
                .subscribe(
                        { item -> pushState(ListViewState.DataReady(item)) },
                        { error -> pushState(ListViewState.Error(error)) }
                )
    }

    override fun onViewEvent(event: ClickEvent<MyItem>) {
        // loads a new color, publishing any state changes
        loadDetailModel()
    }
}