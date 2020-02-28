package com.joaquimverges.helium.navigation.drawer

import com.joaquimverges.helium.core.LogicBlock

/**
 * Presenter Managing a DrawerLayout
 */
class NavDrawerPresenter : LogicBlock<NavDrawerState, NavDrawerEvent>() {

    override fun onUiEvent(event: NavDrawerEvent) {
        // get events via observeViewEvents()
        // or subclass can handle their own events
    }
}