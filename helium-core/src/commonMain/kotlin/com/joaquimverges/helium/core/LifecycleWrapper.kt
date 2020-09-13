package com.joaquimverges.helium.core

import kotlinx.coroutines.CoroutineScope

/**
 * Lifecycle abstraction
 */
expect class LifecycleWrapper {
    val coroutineScope: CoroutineScope
    fun registerLogicBlockForLifecycleEvents(block: LogicBlock<*, *>)
}
