package com.joaquimverges.helium.navigation.toolbar

import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.core.state.BlockState

/**
 * Logic managing a Toolbar
 */
class ToolbarLogic : LogicBlock<BlockState, ToolbarEvent>() {

    override fun onUiEvent(event: ToolbarEvent) {
        // get events via observeViewEvents()
        // or subclass can handle their own events
    }
}