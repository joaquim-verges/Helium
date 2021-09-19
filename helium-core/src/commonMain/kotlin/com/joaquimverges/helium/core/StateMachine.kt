package com.joaquimverges.helium.core

import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.BlockState
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan

fun <S : BlockState, E : BlockEvent> LogicBlock<S, E>.stateMachine(initialState: S, nextState: (S, E) -> S) {
    observeEvents()
        .scan(initialState) { state, event -> nextState(state, event) }
        .onEach { pushState(it) }
        .launchInBlock()
}