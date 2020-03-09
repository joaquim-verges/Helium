package com.joaquimverges.helium.core

import androidx.lifecycle.Lifecycle
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

    fun withChildBlocks(vararg list: AppBlock<*, *>): AppBlock<S, E> {
        childBlocks.addAll(list)
        return this
    }

    fun assemble() {
        val lifecycle: Lifecycle = ui.lifecycle
        logic.observeState().autoDispose(lifecycle).subscribe { ui.render(it) }
        ui.observer().autoDispose(lifecycle).subscribe { logic.processEvent(it) }
        lifecycle.addObserver(logic)

        childBlocks.forEach {
            it.assemble()
        }
    }
}

operator fun <S : BlockState, E : BlockEvent> LogicBlock<S, E>.plus(vd: UiBlock<S, E>) = AppBlock(this, vd)
