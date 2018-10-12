package com.joaquimverges.helium.core.presenter

import android.arch.lifecycle.Lifecycle
import android.support.v4.app.FragmentActivity
import android.view.View
import com.joaquimverges.helium.core.event.ViewEvent
import com.joaquimverges.helium.core.state.ViewState
import com.joaquimverges.helium.core.viewdelegate.BaseViewDelegate
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BasePresenterTest : TestCase() {

    @Mock lateinit var view: View
    @Mock lateinit var context: FragmentActivity
    @Mock lateinit var lifecycle: Lifecycle

    private val viewEvent = SimpleViewEvent()
    private val viewState = SimpleViewState()

    lateinit var presenter: BasePresenter<ViewState, ViewEvent>
    lateinit var viewDelegate: BaseViewDelegate<ViewState, ViewEvent>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        whenever(view.context).thenReturn(context)
        whenever(lifecycle.currentState).thenReturn(Lifecycle.State.CREATED)

        viewDelegate = spy(TestViewDelegate(view))
        presenter = spy(TestPresenter())

        whenever(viewDelegate.lifecycle).thenReturn(lifecycle)
    }

    @Test
    fun testAttach() {
        presenter.attach(viewDelegate)
        verify(lifecycle).addObserver(presenter)
    }

    @Test
    fun testViewState() {
        boostrapAttach()
        presenter.pushState(viewState)
        verify(viewDelegate).render(viewState)
    }

    @Test
    fun testViewStateBeforeAttach() {
        presenter.pushState(viewState)
        verify(viewDelegate, never()).render(viewState)
        boostrapAttach()
        verify(viewDelegate).render(viewState)
    }

    @Test
    fun testViewEvent() {
        boostrapAttach()
        viewDelegate.pushEvent(viewEvent)
        verify(presenter).onViewEvent(viewEvent)
    }

    @Test
    fun testViewEventBeforeAttach() {
        viewDelegate.pushEvent(viewEvent)
        verify(presenter, never()).onViewEvent(viewEvent)
        boostrapAttach()
        verify(presenter, never()).onViewEvent(viewEvent)
    }

    private fun boostrapAttach() {
        presenter.attach(viewDelegate)
    }

    class SimpleViewEvent : ViewEvent
    class SimpleViewState : ViewState

    class TestPresenter : BasePresenter<ViewState, ViewEvent>() {
        override fun onViewEvent(event: ViewEvent) {
        }
    }

    class TestViewDelegate(layout: View) : BaseViewDelegate<ViewState, ViewEvent>(layout) {
        override fun render(viewState: ViewState) {
        }
    }
}
