package com.joaquimvergse.helium.compose

import androidx.compose.runtime.*
import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.event.EventDispatcher
import com.joaquimverges.helium.core.state.BlockState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun <S : BlockState, E : BlockEvent> AppBlock(
    logic: LogicBlock<S, E>,
    ui: @Composable (
        stateFlow: S?,
        eventDispatcher: EventDispatcher<E>
    ) -> Unit
) {
    val state by logic.observeState().collectAsState(initial = logic.currentState())
    val dispatcher = remember(ui) { EventDispatcher<E>() }
    launchInComposition {
        dispatcher.observer()
            .onEach { logic.processEvent(it) }
            .launchIn(this)
    }
    ui(state, dispatcher)
}
