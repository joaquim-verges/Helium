package com.joaquimverges.helium.util

import android.os.SystemClock
import java.util.concurrent.TimeUnit

/**
 * Convenience class to describe a refresh policy.
 *
 * @param refreshInterval The amount of time (in [timeUnit] unit) required between each refreshes
 * @param timeUnit the [TimeUnit] unit for [refreshInterval]
 */
class RefreshPolicy(private val refreshInterval: Long = 0,
                    private val timeUnit: TimeUnit = TimeUnit.MINUTES) {

    private var lastRefreshTime: Long = 0

    fun shouldRefresh(): Boolean = SystemClock.elapsedRealtime() - lastRefreshTime > timeUnit.toMillis(refreshInterval)

    fun updateLastRefreshedTime() {
        lastRefreshTime = SystemClock.elapsedRealtime()
    }
}