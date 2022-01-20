package com.joaquimverges.helium.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

actual abstract class HeliumViewModel {
    actual val coroutineScope: CoroutineScope = MainScope()

    actual open fun onResume() {
    }

    actual open fun onPause() {
    }

    actual open fun onClear() {
    }
}
