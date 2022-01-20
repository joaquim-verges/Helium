package com.joaquimverges.helium.core

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

/**
 * Android ViewModel implementation
 */
actual abstract class HeliumViewModel : ViewModel(), DefaultLifecycleObserver {
    actual val coroutineScope: CoroutineScope = viewModelScope

    override fun onResume(owner: LifecycleOwner) {
        onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        onPause()
    }

    override fun onCleared() {
        super.onCleared()
        this.onClear()
    }

    actual open fun onResume() {
    }

    actual open fun onPause() {
    }

    actual open fun onClear() {
    }
}
