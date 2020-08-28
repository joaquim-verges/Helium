package com.joaquimverges.helium.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope

actual abstract class HeliumViewModel {
    actual val coroutineScope: CoroutineScope = MainScope()
}