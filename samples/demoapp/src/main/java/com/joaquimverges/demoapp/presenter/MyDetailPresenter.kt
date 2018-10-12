package com.joaquimverges.demoapp.presenter

import com.joaquimverges.demoapp.data.MyDdetailRepository
import com.joaquimverges.demoapp.data.MyItem
import com.joaquimverges.helium.core.event.ClickEvent
import com.joaquimverges.helium.core.presenter.BasePresenter
import com.joaquimverges.helium.ui.state.NetworkViewState
import com.joaquimverges.helium.core.util.async

/**
 * @author joaquim
 */
class MyDetailPresenter(private val repository: MyDdetailRepository = MyDdetailRepository())
    : BasePresenter<NetworkViewState<MyItem>, ClickEvent<MyItem>>() {

    init {
        loadDetailModel()
    }

    private fun loadDetailModel() {
        repository
                .getData()
                .async()
                .doOnSubscribe { pushState(NetworkViewState.Loading()) }
                .subscribe(
                        { item -> pushState(NetworkViewState.DataReady(item)) },
                        { error -> pushState(NetworkViewState.Error(error)) }
                )
    }

    override fun onViewEvent(event: ClickEvent<MyItem>) {
        // loads a new color, publishing any state changes
        loadDetailModel()
    }
}