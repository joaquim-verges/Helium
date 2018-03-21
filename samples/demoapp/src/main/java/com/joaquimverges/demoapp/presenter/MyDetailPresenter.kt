package com.joaquimverges.demoapp.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import com.joaquimverges.demoapp.data.MyDdetailRepository
import com.joaquimverges.demoapp.data.MyItem
import com.joaquimverges.helium.event.ClickEvent
import com.joaquimverges.helium.presenter.BasePresenter
import com.joaquimverges.helium.state.NetworkViewState
import com.joaquimverges.helium.util.async

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