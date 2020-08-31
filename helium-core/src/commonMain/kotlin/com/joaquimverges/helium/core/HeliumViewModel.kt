package com.joaquimverges.helium.core

import kotlinx.coroutines.CoroutineScope

/**
 * @author joaquim
 */
expect abstract class HeliumViewModel() {
    val coroutineScope: CoroutineScope
}
