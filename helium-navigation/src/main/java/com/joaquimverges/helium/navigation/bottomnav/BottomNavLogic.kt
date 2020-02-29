package com.joaquimverges.helium.navigation.bottomnav

import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.core.state.BlockState

/**
 * Logic managing a BottomNavUi
 */
open class BottomNavLogic : LogicBlock<BlockState, BottomNavEvent>() {

    override fun onUiEvent(event: BottomNavEvent) {
        // get events via observeViewEvents()
        // or subclass can handle their own events
    }
}