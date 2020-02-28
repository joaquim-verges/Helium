package com.joaquimverges.helium.core.retained

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Kotlin-friendly Wrapper around ViewModelProvider.Factory
 */
class LogicBlockFactory<T : ViewModel?>(
    private val clazz: Class<T>,
    private val factory: (Class<T>) -> T
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (clazz.isAssignableFrom(modelClass)) {
            @Suppress("UNCHECKED_CAST")
            return factory.invoke(clazz) as T
        }
        throw IllegalArgumentException("Unknown Presenter class: $modelClass")
    }
}