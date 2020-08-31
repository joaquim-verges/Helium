package com.joaquimverges.helium.core

import kotlinx.coroutines.CoroutineScope

/**
 * @author joaquim
 */
expect class LifecycleWrapper {
    val coroutineScope: CoroutineScope
    fun registerLogicBlockForLifecycleEvents(block: LogicBlock<*, *>)
}
