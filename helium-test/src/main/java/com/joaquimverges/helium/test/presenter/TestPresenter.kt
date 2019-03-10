package com.joaquimverges.helium.test.presenter

import com.joaquimverges.helium.core.event.ViewEvent
import com.joaquimverges.helium.core.presenter.BasePresenter
import com.joaquimverges.helium.core.state.ViewState
import org.mockito.internal.matchers.apachecommons.ReflectionEquals

class TestPresenter<S : ViewState, E : ViewEvent> : BasePresenter<S, E>() {

    private var lastReceivedEvent: ViewEvent? = null
    private var lastViewState: ViewState? = null

    init {
        observeViewState().subscribe {
            lastViewState = it
        }.autoDispose()
    }

    override fun onViewEvent(event: E) {
        lastReceivedEvent = event
    }

    fun assertState(state: ViewState) {
        assert(ReflectionEquals(state).matches(lastViewState))
    }

    fun assertOnEvent(event: ViewEvent) {
        assert(ReflectionEquals(event).matches(lastReceivedEvent))
    }

    fun assertNoEvents() {
        if (lastReceivedEvent != null) {
            throw IllegalStateException("Expected no events received but got: $lastReceivedEvent")
        }
    }
}
