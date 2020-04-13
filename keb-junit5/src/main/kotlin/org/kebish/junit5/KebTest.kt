package org.kebish.junit5

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.kebish.core.Configuration
import org.kebish.core.test.KebTestBase

abstract class KebTest(config: Configuration) : KebTestBase(config) {

    @BeforeEach
    fun beforeEachTestJunit5() {
        super.beforeEachTest()
    }

    @AfterEach
    fun afterEachTestJunit5() {
        super.afterEachTest()
    }
}