package com.joaquimverges.helium.core

import androidx.lifecycle.Lifecycle
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.BlockState
import com.joaquimverges.helium.core.util.autoDispose

/**
 * Class responsible for connecting Presenters and ViewDelegates together.
 */
class AppBlock<S : BlockState, E : BlockEvent>(
    private val presenter: LogicBlock<S, E>,
    private val viewDelegate: UiBlock<S, E>,
    private val childBlocks: List<AppBlock<*, *>> = emptyList()
) {

    fun assemble() {
        val lifecycle: Lifecycle = viewDelegate.lifecycle
        presenter.observeState().autoDispose(lifecycle).subscribe { viewDelegate.render(it) }
        viewDelegate.observer().autoDispose(lifecycle).subscribe { presenter.processEvent(it) }
        lifecycle.addObserver(presenter)

        childBlocks.forEach {
            it.assemble()
        }
    }
}

operator fun <S : BlockState, E : BlockEvent> LogicBlock<S, E>.plus(vd: UiBlock<S, E>) = AppBlock(this, vd)
