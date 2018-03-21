package com.joaquimverges.helium.event

import android.view.View

/**
 * Convenience for delegates with only one click event
 */
class ClickEvent<out T>(val view: View, val data: T) : ViewEvent