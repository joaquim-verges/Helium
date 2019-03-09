package com.joaquimverges.helium.test

import com.joaquimverges.helium.core.event.ViewEvent
import com.joaquimverges.helium.core.state.ViewState
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CoreTests : TestCase() {

    class SimpleViewEvent : ViewEvent
    class SimpleViewState : ViewState

    private val viewEvent = SimpleViewEvent()
    private val viewState = SimpleViewState()

    private lateinit var presenter: TestPresenter<SimpleViewState, SimpleViewEvent>
    private lateinit var viewDelegate: TestViewDelegate<SimpleViewState, SimpleViewEvent>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewDelegate = com.joaquimverges.helium.test.TestViewDelegate()
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
        viewDelegate.assertHasRendered(viewState)
    }

    @Test
    fun testViewStateBeforeAttach() {
        presenter.pushState(viewState)
        presenter.assertState(viewState)
        viewDelegate.assertNothingRendered()
        bootstrapAttach()
        viewDelegate.assertHasRendered(viewState)
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
