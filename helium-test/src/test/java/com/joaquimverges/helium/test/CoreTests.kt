package com.joaquimverges.helium.test

import com.joaquimverges.helium.core.event.ViewEvent
import com.joaquimverges.helium.core.state.ViewState
import com.joaquimverges.helium.test.presenter.TestPresenter
import com.joaquimverges.helium.test.viewdelegate.TestViewDelegate
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class CoreTests : HeliumTestCase() {

    class SimpleViewEvent : ViewEvent
    class SimpleViewState : ViewState

    private val viewEvent = SimpleViewEvent()
    private val viewState = SimpleViewState()

    private lateinit var presenter: TestPresenter<SimpleViewState, SimpleViewEvent>
    private lateinit var viewDelegate: TestViewDelegate<SimpleViewState, SimpleViewEvent>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewDelegate = TestViewDelegate()
        presenter = TestPresenter()
    }

    @Test
    fun testAttach() {
        presenter.attach(viewDelegate)
        viewDelegate.assertAttached(presenter)
    }

    @Test
    fun testViewState() {
        bootstrapAttach()
        presenter.pushState(viewState)
        presenter.assertState(viewState)
        viewDelegate.assertLastRendered(viewState)
    }

    @Test
    fun testViewStateBeforeAttach() {
        presenter.pushState(viewState)
        presenter.assertState(viewState)
        viewDelegate.assertNothingRendered()
        bootstrapAttach()
        viewDelegate.assertLastRendered(viewState)
    }

    @Test
    fun testViewEvent() {
        bootstrapAttach()
        viewDelegate.pushEvent(viewEvent)
        presenter.assertOnEvent(viewEvent)
    }

    @Test
    fun testViewEventBeforeAttach() {
        viewDelegate.pushEvent(viewEvent)
        presenter.assertNoEvents()
        bootstrapAttach()
        presenter.assertNoEvents()
    }

    private fun bootstrapAttach() {
        presenter.attach(viewDelegate)
    }

}
