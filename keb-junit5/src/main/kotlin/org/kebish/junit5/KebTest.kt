package org.kebish.junit5

import org.junit.jupiter.api.AfterEach
import org.kebish.core.Configuration
import org.kebish.core.test.KebTestBase

 abstract class KebTest(config: Configuration) : KebTestBase(config) {

    @AfterEach
    fun afterEachTestJunit5() {
        super.afterEachTest()
    }
}