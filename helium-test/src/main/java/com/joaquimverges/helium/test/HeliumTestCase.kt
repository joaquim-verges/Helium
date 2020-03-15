package com.joaquimverges.helium.test

import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.joaquimverges.helium.core.AppBlock
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.BlockState
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import junit.framework.TestCase
import org.junit.Rule
import org.junit.runner.RunWith

/**
 * Convenience base class for unit testing Helium blocks
 */
@Suppress("LeakingThis")
@RunWith(AndroidJUnit4::class)
open class HeliumTestCase : TestCase() {
    @get:Rule var mockRule = MockitoInitializationRule(this)
    @get:Rule var rxRule = RxSchedulerRule()

    /**
     * Assemble blocks with a mocked lifecycle
     */
    fun <S: BlockState, E: BlockEvent> assemble(appBlock: AppBlock<S, E>) {
        appBlock.assemble(getMockLifecycle())
    }

    /**
     * Convenience method to mock a lifecycle with a given state.
     */
    fun getMockLifecycle(state: Lifecycle.State = Lifecycle.State.CREATED): Lifecycle {
        return mock<Lifecycle>().apply {
            whenever(currentState).thenReturn(state)
        }
    }
}