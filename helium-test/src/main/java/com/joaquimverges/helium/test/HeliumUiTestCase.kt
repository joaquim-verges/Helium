package com.joaquimverges.helium.test

import android.app.Activity
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.ext.junit.rules.activityScenarioRule
import org.junit.Rule

/**
 * Convenience base class for testing UiBlocks, provides a real Activity and can test espresso matchings.
 */
open class HeliumUiTestCase : HeliumTestCase() {

    @get:Rule var activityScenarioRule = activityScenarioRule<TestActivity>()

    fun getActivityScenario() = activityScenarioRule.scenario

    fun onActivity(block: (Activity) -> Unit) {
        getActivityScenario().onActivity(block)
    }

    fun verifyViewVisible(@IdRes viewResId: Int) {
        Espresso.onView(ViewMatchers.withId(viewResId))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    fun verifyViewHidden(@IdRes viewResId: Int) {
        Espresso.onView(ViewMatchers.withId(viewResId))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }
}