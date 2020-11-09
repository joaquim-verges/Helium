package com.joaquimverges.helium.test

import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.joaquimverges.helium.core.AppBlock
import com.joaquimverges.helium.core.LifecycleWrapper
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.BlockState
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/**
 * Convenience base class for unit testing Helium blocks
 */
@Suppress("LeakingThis")
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
open class HeliumTestCase : TestCase() {
    @get:Rule var mockRule = MockitoInitializationRule(this)
    @get:Rule var coroutinesTestRule = CoroutinesTestRule()

    val testCoroutineScope: CoroutineScope = TestCoroutineScope(coroutinesTestRule.testDispatcher)

    /**
     * Assemble blocks with a mocked lifecycle
     */
    fun <S : BlockState, E : BlockEvent> assemble(appBlock: AppBlock<S, E>) {
        appBlock.assemble(LifecycleWrapper(getMockLifecycle(), testCoroutineScope))
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
