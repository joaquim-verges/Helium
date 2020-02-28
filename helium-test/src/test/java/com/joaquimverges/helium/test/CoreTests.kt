package com.joaquimverges.helium.test

import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.BlockState
import com.joaquimverges.helium.test.presenter.TestLogicBLock
import com.joaquimverges.helium.test.viewdelegate.TestUiBlock
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class CoreTests : HeliumTestCase() {

    class SimpleBlockEvent : BlockEvent
    class SimpleBlockState : BlockState

    private val viewEvent = SimpleBlockEvent()
    private val viewState = SimpleBlockState()

    private lateinit var logicBLock: TestLogicBLock<SimpleBlockState, SimpleBlockEvent>
    private lateinit var uiBlock: TestUiBlock<SimpleBlockState, SimpleBlockEvent>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        uiBlock = TestUiBlock()
        logicBLock = TestLogicBLock()
    }

    @Test
    fun testAttach() {
        logicBLock.attach(uiBlock)
        uiBlock.assertAttached(logicBLock)
    }

    @Test
    fun testViewState() {
        bootstrapAttach()
        logicBLock.pushState(viewState)
        logicBLock.assertState(viewState)
        uiBlock.assertLastRendered(viewState)
    }

    @Test
    fun testViewStateBeforeAttach() {
        logicBLock.pushState(viewState)
        logicBLock.assertState(viewState)
        uiBlock.assertNothingRendered()
        bootstrapAttach()
        uiBlock.assertLastRendered(viewState)
    }

    @Test
    fun testViewEvent() {
        bootstrapAttach()
        uiBlock.pushEvent(viewEvent)
        logicBLock.assertOnEvent(viewEvent)
    }

    @Test
    fun testViewEventBeforeAttach() {
        uiBlock.pushEvent(viewEvent)
        logicBLock.assertNoEvents()
        bootstrapAttach()
        logicBLock.assertNoEvents()
    }

    private fun bootstrapAttach() {
        logicBLock.attach(uiBlock)
    }

}
