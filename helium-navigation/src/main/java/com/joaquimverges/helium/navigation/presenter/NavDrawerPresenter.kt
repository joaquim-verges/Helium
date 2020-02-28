package com.joaquimverges.helium.navigation.presenter

import com.joaquimverges.helium.core.BasePresenter
import com.joaquimverges.helium.navigation.event.NavDrawerEvent
import com.joaquimverges.helium.navigation.state.NavDrawerState

/**
 * Presenter Managing a DrawerLayout
 */
class NavDrawerPresenter : BasePresenter<NavDrawerState, NavDrawerEvent>() {

    override fun onViewEvent(event: NavDrawerEvent) {
        // get events via observeViewEvents()
        // or subclass can handle their own events
    }
}