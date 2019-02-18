package com.joaquimverges.demoapp.data

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

/**
 * @author joaquim
 */
object Colors {

    private val colors = intArrayOf(
            android.R.color.holo_blue_light,
            android.R.color.holo_blue_dark,
            android.R.color.holo_green_dark,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_orange_dark,
            android.R.color.holo_red_light,
            android.R.color.holo_red_dark)

    @ColorRes
    fun randomColor(position: Int) = colors[Math.abs(position) % colors.size]

    fun toHexString(@ColorInt color: Int) = String.format("#%06X", (0xFFFFFF and color))
}