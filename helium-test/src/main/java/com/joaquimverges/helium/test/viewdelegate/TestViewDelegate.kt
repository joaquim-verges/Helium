package com.joaquimverges.helium.test.viewdelegate

import android.content.Context
import android.view.View
import androidx.lifecycle.Lifecycle
import com.joaquimverges.helium.core.event.ViewEvent
import com.joaquimverges.helium.core.presenter.BasePresenter
import com.joaquimverges.helium.core.state.ViewState
import com.joaquimverges.helium.core.viewdelegate.BaseViewDelegate
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.fail
import org.mockito.internal.matchers.apachecommons.ReflectionEquals

/**
 * Utility class for unit testing presenters with a generic view delegate.
 * Has handy methods to assert last rendered states.
 */
class TestViewDelegate<S : ViewState, E : ViewEvent>(
    mockView: View = mock(),
    mockContext: Context = mock(),
    private val mockLifecycle: Lifecycle = mock<Lifecycle>().apply {
        whenever(currentState).thenReturn(Lifecycle.State.CREATED)
    }
) : BaseViewDelegate<S, E>(
    view = mockView,
    context = mockContext
) {
    private var lastRenderedState: ViewState? = null

    override fun render(viewState: S) {
        lastRenderedState = viewState
    }

    override fun getLifecycle(): Lifecycle {
        return mockLifecycle
    }

    fun assertLastRendered(state: ViewState) {
        if (!ReflectionEquals(lastRenderedState).matches(state)) {
            fail("assertLastRendered failed\n\nExpected:\n\n$state\n\nbut last state rendered was:\n\n$lastRenderedState")
        }
    }

    fun assertNothingRendered() {
        if (lastRenderedState != null) {
            fail("Expected no state rendered but actually rendered: $lastRenderedState")
        }
    }

    fun assertAttached(presenter: BasePresenter<S, E>) {
        verify(mockLifecycle).addObserver(presenter)
    }
}
