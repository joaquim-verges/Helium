package com.joaquimverges.helium.test.viewdelegate

import android.content.Context
import android.view.View
import androidx.lifecycle.Lifecycle
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.LogicBlock
import com.joaquimverges.helium.core.state.BlockState
import com.joaquimverges.helium.core.UiBlock
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.fail
import org.mockito.internal.matchers.apachecommons.ReflectionEquals

/**
 * Utility class for unit testing presenters with a generic view delegate.
 * Has handy methods to assert last rendered states.
 */
class TestViewDelegate<S : BlockState, E : BlockEvent>(
    mockView: View = mock(),
    mockContext: Context = mock(),
    private val mockLifecycle: Lifecycle = mock<Lifecycle>().apply {
        whenever(currentState).thenReturn(Lifecycle.State.CREATED)
    }
) : UiBlock<S, E>(
    view = mockView,
    context = mockContext
) {
    private var lastRenderedState: BlockState? = null

    override fun render(viewState: S) {
        lastRenderedState = viewState
    }

    override fun getLifecycle(): Lifecycle {
        return mockLifecycle
    }

    fun assertLastRendered(state: BlockState) {
        if (!ReflectionEquals(lastRenderedState).matches(state)) {
            fail("assertLastRendered failed\n\nExpected:\n\n$state\n\nbut last state rendered was:\n\n$lastRenderedState")
        }
    }

    fun assertNothingRendered() {
        if (lastRenderedState != null) {
            fail("Expected no state rendered but actually rendered: $lastRenderedState")
        }
    }

    fun assertAttached(presenter: LogicBlock<S, E>) {
        verify(mockLifecycle).addObserver(presenter)
    }
}
