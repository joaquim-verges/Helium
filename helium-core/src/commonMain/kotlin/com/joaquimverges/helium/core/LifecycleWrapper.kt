package com.joaquimverges.helium.core

import kotlinx.coroutines.CoroutineScope

/**
 * @author joaqu
 */
expect class LifecycleWrapper {
    val coroutineScope: CoroutineScope
    fun registerLogicBlockForLifecycleEvents(block: LogicBlock<*, *>)
}