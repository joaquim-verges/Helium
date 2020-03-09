package com.joaquimverges.helium.test

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.mockito.MockitoAnnotations

class MockitoInitializationRule(testClass: Any) : TestRule {

    init {
        MockitoAnnotations.initMocks(testClass)
    }

    override fun apply(base: Statement, description: Description?): Statement {
        return base
    }
}
