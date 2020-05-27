package com.joaquimverges.helium.test

import android.content.Context
import android.view.View
import com.joaquimverges.helium.core.UiBlock
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.BlockState
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.fail
import org.mockito.internal.matchers.apachecommons.ReflectionEquals

/**
 * Utility class for unit testing a LogicBlock with a generic UiBlock.
 * Has handy methods to assert last rendered states.
 */
class TestUiBlock<S : BlockState, E : BlockEvent>(
    mockView: View = mock(),
    mockContext: Context = mock()
) : UiBlock<S, E>(
    view = mockView,
    context = mockContext
) {
    private var lastRenderedState: BlockState? = null

    override fun render(state: S) {
        lastRenderedState = state
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
}
