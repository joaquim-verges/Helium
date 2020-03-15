package com.joaquimverges.helium.core

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.BlockState
import com.joaquimverges.helium.core.util.autoDispose

/**
 * Class responsible for assembling LogicBlocks and UiBlocks together.
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
    fun assemble(lifecycle: Lifecycle) {
        logic.observeState().autoDispose(lifecycle).subscribe { ui.render(it) }
        ui.observer().autoDispose(lifecycle).subscribe { logic.processEvent(it) }
        lifecycle.addObserver(logic)

        childBlocks.forEach {
            it.assemble(lifecycle)
        }
    }
}

/**
 * Convenience operator to create an AppBlock.
 * Usage : (LogicBlock + UIBlock)
 */
operator fun <S : BlockState, E : BlockEvent> LogicBlock<S, E>.plus(vd: UiBlock<S, E>) = AppBlock(this, vd)

/**
 * Convenience extension to assemble an AppBlock from any LifecycleOwner (Activity, Fragment, etc)
 */
fun <S : BlockState, E : BlockEvent> LifecycleOwner.assemble(appBlock: AppBlock<S, E>) {
    val lifecycle = when (this) {
        is Fragment -> this.viewLifecycleOwner.lifecycle
        else -> this.lifecycle
    }
    appBlock.assemble(lifecycle)
}

