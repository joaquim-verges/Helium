package com.joaquimverges.kmp.news.android

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.joaquimverges.helium.core.EventDispatcher
import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.BlockState
import kotlinx.coroutines.flow.onEach

@Composable
fun <S : BlockState, E : BlockEvent> AppBlock(
        logic: LogicBlock<S, E>,
        ui: @Composable (
                stateFlow: S?,
                eventDispatcher: EventDispatcher<E>
        ) -> Unit) {
    val state by logic.observeState().collectAsState(initial = logic.currentState())
    ui(state, logic.eventDispatcher)
}