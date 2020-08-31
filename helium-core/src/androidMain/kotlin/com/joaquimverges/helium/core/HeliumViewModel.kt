package com.joaquimverges.helium.core

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

/**
 * @author joaquim
 */
actual abstract class HeliumViewModel : ViewModel(), LifecycleObserver {
    actual val coroutineScope: CoroutineScope = viewModelScope
}
