package com.joaquimverges.helium.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope

actual abstract class HeliumViewModel {
    actual val coroutineScope: CoroutineScope = GlobalScope
}