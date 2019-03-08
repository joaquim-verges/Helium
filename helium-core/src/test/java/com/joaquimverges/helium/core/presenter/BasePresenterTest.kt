package com.joaquimverges.helium.core.presenter

import android.content.Context
import android.view.View
import androidx.lifecycle.Lifecycle
import com.joaquimverges.helium.core.event.ViewEvent
import com.joaquimverges.helium.core.state.ViewState
import com.joaquimverges.helium.core.viewdelegate.BaseViewDelegate
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BasePresenterTest : TestCase() {

    private val viewEvent = SimpleViewEvent()
    private val viewState = SimpleViewState()

    private lateinit var presenter: TestPresenter
    private lateinit var viewDelegate: TestViewDelegate

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
        boostrapAttach()
        presenter.pushState(viewState)
        presenter.assertState(viewState)
        viewDelegate.assertHasRendered(viewState)
    }

    @Test
    fun testViewStateBeforeAttach() {
        presenter.pushState(viewState)
        presenter.assertState(viewState)
        viewDelegate.assertNothingRendered()
        boostrapAttach()
        viewDelegate.assertHasRendered(viewState)
    }

    @Test
    fun testViewEvent() {
        boostrapAttach()
        viewDelegate.pushEvent(viewEvent)
        presenter.assertOnEvent(viewEvent)
    }

    @Test
    fun testViewEventBeforeAttach() {
        viewDelegate.pushEvent(viewEvent)
        presenter.assertNoEvents()
        boostrapAttach()
        presenter.assertNoEvents()
    }

    private fun boostrapAttach() {
        presenter.attach(viewDelegate)
    }

    class SimpleViewEvent : ViewEvent
    class SimpleViewState : ViewState

    class TestPresenter : BasePresenter<ViewState, ViewEvent>() {

        private var lastReceivedEvent: ViewEvent? = null
        private var lastViewState: ViewState? = null

        init {
            observeViewState().subscribe {
                lastViewState = it
            }
        }

        override fun onViewEvent(event: ViewEvent) {
            lastReceivedEvent = event
        }

        fun assertState(state: ViewState) {
            assertEquals(lastViewState, state)
        }

        fun assertOnEvent(event: ViewEvent) {
            assertEquals(lastReceivedEvent, event)
        }

        fun assertNoEvents() {
            assertEquals(lastReceivedEvent, null)
        }
    }

    class TestViewDelegate(
        mockView: View = mock(),
        mockContext: Context = mock(),
        private val mockLifecycle: Lifecycle = mock<Lifecycle>().apply {
            whenever(currentState).thenReturn(Lifecycle.State.CREATED)
        }
    ) : BaseViewDelegate<ViewState, ViewEvent>(
        view = mockView,
        context = mockContext,
        lifecycle = mockLifecycle
    ) {
        private var lastRenderedState: ViewState? = null

        override fun render(viewState: ViewState) {
            lastRenderedState = viewState
        }

        fun assertHasRendered(state: ViewState) {
            assertEquals(lastRenderedState, state)
        }

        fun assertNothingRendered() {
            assertEquals(lastRenderedState, null)
        }

        fun assertAttached(presenter: BasePresenter<ViewState, ViewEvent>) {
            verify(mockLifecycle).addObserver(presenter)
        }
    }
}
