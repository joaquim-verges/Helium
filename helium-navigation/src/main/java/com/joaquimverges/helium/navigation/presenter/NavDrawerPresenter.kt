package com.joaquimverges.helium.navigation.presenter

import com.joaquimverges.helium.core.presenter.BasePresenter
import com.joaquimverges.helium.navigation.event.NavDrawerEvent
import com.joaquimverges.helium.navigation.state.NavDrawerState

/**
 * @author joaquim
 */
class NavDrawerPresenter : BasePresenter<NavDrawerState, NavDrawerEvent>() {

    override fun onViewEvent(event: NavDrawerEvent) {
        // might want to do actions on drawer open / closed
    }
}