package com.joaquimverges.helium.core.util

import android.view.View

/**
 * Convenience extension function to execute something when a view is attached to the window
 */
fun View.onAttached(block: () -> Unit) {
    addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(p0: View?) {
            removeOnAttachStateChangeListener(this)
            block()
        }

        override fun onViewDetachedFromWindow(p0: View?) {
            // no-op
        }
    })
}