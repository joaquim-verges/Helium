package com.joaquimverges.helium.test

import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.Rule
import org.junit.runner.RunWith

/**
 * Convenience base class for unit testing Helium blocks
 */
@Suppress("LeakingThis")
@RunWith(AndroidJUnit4::class)
open class HeliumTestCase : TestCase() {
    @get:Rule var mockRule = MockitoInitializationRule(this)
    @get:Rule var rxRule = RxSchedulerRule()
}