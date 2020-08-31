package com.joaquimverges.helium.core.state

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

class StateObserver<S> {
    private val stateFlow: MutableStateFlow<S?> = MutableStateFlow(null)
    var currentState: S? = null
        get() = stateFlow.value
        private set

    val observer: Flow<S> = stateFlow.filterNotNull()

    fun pushState(state: S) {
        stateFlow.value = state
    }
}
