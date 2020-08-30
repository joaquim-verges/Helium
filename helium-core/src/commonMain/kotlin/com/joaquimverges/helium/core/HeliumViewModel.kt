package com.joaquimverges.helium.core

import kotlinx.coroutines.CoroutineScope

/**
 * @author joaqu
 */
expect abstract class HeliumViewModel() {
    val coroutineScope: CoroutineScope
}
