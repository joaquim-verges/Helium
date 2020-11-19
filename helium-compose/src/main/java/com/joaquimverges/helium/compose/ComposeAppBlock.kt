package com.joaquimverges.helium.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
    val scope = rememberCoroutineScope()
    val dispatcher = remember(ui) {
        EventDispatcher<E>().apply {
            observer()
                .onEach { logic.processEvent(it) }
                .launchIn(scope)
        }
    }
    ui(state, dispatcher)
}
