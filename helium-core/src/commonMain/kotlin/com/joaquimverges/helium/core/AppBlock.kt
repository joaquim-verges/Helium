package com.joaquimverges.helium.core

import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.BlockState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * @author joaqu
 */
class AppBlock<S : BlockState, E : BlockEvent>(
    private val logic: LogicBlock<S, E>,
    private val ui: UiBlock<S, E>
) {
    private val childBlocks: MutableList<AppBlock<*, *>> = mutableListOf()

    /**
     * It's common for AppBlocks to contain smaller AppBlocks, which also need to be assembled.
     * Declaring these sub AppBlocks (in the form of a sub logic block and a sub UI block)
     * this way will ensure that they all get assembled when their parent gets assembled.
     */
    fun withChildBlocks(vararg list: AppBlock<*, *>): AppBlock<S, E> {
        childBlocks.addAll(list)
        return this
    }

    /**
     * This is where the connection between Logic and UI happens.
     * Once this is called, LogicBlock will receive events from the UIBlock, and UIBlock will receive state updates from the LogicBlock.
     * This also enables the LogicBlock to receive lifecycle events, by annotating functions with @OnLifecycleEvent.
     */
    fun assemble(lifecycleWrapper: LifecycleWrapper) {
        logic.observeState().onEach { ui.render(it) }.launchIn(lifecycleWrapper.coroutineScope)
        ui.observer().onEach { logic.processEvent(it) }.launchIn(lifecycleWrapper.coroutineScope)
        lifecycleWrapper.registerLogicBlockForLifecycleEvents(logic)

        childBlocks.forEach {
            it.assemble(lifecycleWrapper)
        }
    }
}


/**
 * Convenience operator to create an AppBlock.
 * Usage : (LogicBlock + UIBlock)
 */
operator fun <S : BlockState, E : BlockEvent> LogicBlock<S, E>.plus(vd: UiBlock<S, E>) = AppBlock(this, vd)
