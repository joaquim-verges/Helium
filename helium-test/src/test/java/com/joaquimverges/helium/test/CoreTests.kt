package com.joaquimverges.helium.test

import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.BlockState
import com.joaquimverges.helium.test.presenter.TestPresenter
import com.joaquimverges.helium.test.viewdelegate.TestViewDelegate
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class CoreTests : HeliumTestCase() {

    class SimpleBlockEvent : BlockEvent
    class SimpleBlockState : BlockState

    private val viewEvent = SimpleBlockEvent()
    private val viewState = SimpleBlockState()

    private lateinit var presenter: TestPresenter<SimpleBlockState, SimpleBlockEvent>
    private lateinit var viewDelegate: TestViewDelegate<SimpleBlockState, SimpleBlockEvent>

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
