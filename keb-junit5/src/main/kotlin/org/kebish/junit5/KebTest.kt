package org.kebish.junit5

import org.kebish.core.Browser
import org.kebish.core.test.KebTestBase
import org.junit.jupiter.api.AfterEach

 abstract class KebTest(browser: Browser) : KebTestBase(browser) {

    @AfterEach
    fun afterEachTestJunit5() {
        super.afterEachTest()
    }
}