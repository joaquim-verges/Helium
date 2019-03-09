package com.joaquimverges.helium.test

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
import org.mockito.internal.matchers.apachecommons.ReflectionEquals

class TestViewDelegate<S : ViewState, E : ViewEvent>(
    mockView: View = mock(),
    mockContext: Context = mock(),
    private val mockLifecycle: Lifecycle = mock<Lifecycle>().apply {
        whenever(currentState).thenReturn(Lifecycle.State.CREATED)
    }
) : BaseViewDelegate<S, E>(
    view = mockView,
    context = mockContext,
    lifecycle = mockLifecycle
) {
    private var lastRenderedState: ViewState? = null

    override fun render(viewState: S) {
        lastRenderedState = viewState
    }

    fun assertHasRendered(state: ViewState) {
        assert(ReflectionEquals(state).matches(lastRenderedState))
    }

    fun assertNothingRendered() {
        if (lastRenderedState != null) {
            throw IllegalStateException("Expected no state rendered but actually rendered: $lastRenderedState")
        }
    }

    fun assertAttached(presenter: BasePresenter<S, E>) {
        verify(mockLifecycle).addObserver(presenter)
    }
}
