package com.joaquimverges.helium.navigation.drawer

import com.joaquimverges.helium.core.LogicBlock

/**
 * LogicBlock Managing a DrawerLayout
 */
class NavDrawerLogic : LogicBlock<NavDrawerState, NavDrawerEvent>() {

    override fun onUiEvent(event: NavDrawerEvent) {
        // get events via observeViewEvents()
        // or subclass can handle their own events
    }
}