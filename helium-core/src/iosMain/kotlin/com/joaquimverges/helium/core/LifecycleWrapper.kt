package com.joaquimverges.helium.core

import kotlinx.coroutines.CoroutineScope

/**
 * Lifecycle wrapper for iOS
 */
actual class LifecycleWrapper {
    actual val coroutineScope: CoroutineScope = MainScope()

    actual fun registerLogicBlockForLifecycleEvents(block: LogicBlock<*, *>) {
        // TODO
    }
}
