package com.joaquimverges.helium.navigation.presenter

import com.joaquimverges.helium.core.presenter.BasePresenter
import com.joaquimverges.helium.core.state.ViewState
import com.joaquimverges.helium.navigation.event.ToolbarEvent

/**
 * Presenter managing a Toolbar
 */
class ToolbarPresenter : BasePresenter<ViewState, ToolbarEvent>() {

    override fun onViewEvent(event: ToolbarEvent) {
        // get events via observeViewEvents()
        // or subclass can handle their own events
    }
}