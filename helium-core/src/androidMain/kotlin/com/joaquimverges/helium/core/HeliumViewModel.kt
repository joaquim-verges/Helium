package com.joaquimverges.helium.core

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

/**
 * @author joaqu
 */
actual abstract class HeliumViewModel: ViewModel(), LifecycleObserver {
    actual val coroutineScope: CoroutineScope = viewModelScope
}