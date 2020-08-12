package com.joaquimverges.helium.core

import kotlinx.coroutines.*
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlin.coroutines.CoroutineContext

/**
 * Lifecycle wrapper for iOS
 */
actual class LifecycleWrapper {
    actual val coroutineScope: CoroutineScope = MainScope()

    actual fun registerLogicBlockForLifecycleEvents(block: LogicBlock<*, *>) {
        // TODO
    }

    private class MainDispatcher: CoroutineDispatcher() {
        override fun dispatch(context: CoroutineContext, block: Runnable) {
            dispatch_async(dispatch_get_main_queue()) { block.run() }
        }
    }

    internal class MainScope: CoroutineScope {
        private val dispatcher = MainDispatcher()
        private val job = Job()

        override val coroutineContext: CoroutineContext
            get() = dispatcher + job
    }
}
