package com.jv.news.util

import android.os.Build

/**
 * @author joaquim
 */
object VersionUtil {
    fun isAtLeastApi(version: Int): Boolean {
        return Build.VERSION.SDK_INT >= version
    }
}