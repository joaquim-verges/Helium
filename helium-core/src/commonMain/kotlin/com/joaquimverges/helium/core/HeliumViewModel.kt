package com.joaquimverges.helium.core

import kotlinx.coroutines.CoroutineScope

/**
 * Common ViewModel abstraction
 */
expect abstract class HeliumViewModel() {
    val coroutineScope: CoroutineScope
}
