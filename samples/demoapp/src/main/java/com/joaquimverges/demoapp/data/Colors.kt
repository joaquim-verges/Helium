package com.joaquimverges.demoapp.data

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

/**
 * @author joaquim
 */
object Colors {

    enum class HoloColor(@ColorRes val color: Int) {
        LIGHT_BLUE(android.R.color.holo_blue_light),
        DARK_BLUE(android.R.color.holo_blue_dark),
        DARK_GREEN(android.R.color.holo_green_dark),
        LIGHT_GREEN(android.R.color.holo_green_light),
        LIGHT_ORANGE(android.R.color.holo_orange_light),
        DARK_ORANGE(android.R.color.holo_orange_dark),
        LIGHT_RED(android.R.color.holo_red_light),
        DARK_RED(android.R.color.holo_red_dark)
    }

    fun randomColor(position: Int) = HoloColor.values()[Math.abs(position) % HoloColor.values().size]
    fun toHexString(@ColorInt color: Int) = String.format("#%06X", (0xFFFFFF and color))
}
