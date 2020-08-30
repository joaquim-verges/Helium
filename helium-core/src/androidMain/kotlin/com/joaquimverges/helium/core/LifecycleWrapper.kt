package com.joaquimverges.helium.core

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.BlockState
import kotlinx.coroutines.CoroutineScope

/**
 * @author joaqu
 */
actual class LifecycleWrapper(
    private val lifecycle: Lifecycle,
    private val scope: CoroutineScope = lifecycle.coroutineScope
) {
    actual val coroutineScope: CoroutineScope = scope

    actual fun registerLogicBlockForLifecycleEvents(block: LogicBlock<*, *>) {
        lifecycle.addObserver(block)
    }
}

/**
 * Convenience extension to assemble an AppBlock from any LifecycleOwner (Activity, Fragment, etc)
 */
fun <S : BlockState, E : BlockEvent> LifecycleOwner.assemble(appBlock: AppBlock<S, E>) {
    val lifecycle = when (this) {
        is Fragment -> this.viewLifecycleOwner.lifecycle
        else -> this.lifecycle
    }
    appBlock.assemble(LifecycleWrapper(lifecycle))
}

/**
 * Convenience method for assembling a logic and ui block, with no children blocks, from any LifecycleOwner (Activity, Fragment, etc)
 */
fun <S : BlockState, E : BlockEvent> LifecycleOwner.assemble(logic: LogicBlock<S, E>, ui: UiBlock<S, E>) {
    assemble(logic + ui)
}
