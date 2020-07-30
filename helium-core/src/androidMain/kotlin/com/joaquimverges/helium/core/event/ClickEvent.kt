package com.joaquimverges.helium.core.event

import android.view.View

/**
 * Convenience for delegates with only one click event
 */
class ClickEvent<out T>(val view: View, val data: T) : BlockEvent