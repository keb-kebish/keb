package org.kebish.core.browser.management

import org.junit.jupiter.api.Test
import org.kebish.core.config.kebConfig
import org.kebish.junit5.KebTest

class DoNotStartBrowserIfNotNeededTest : KebTest(kebConfig {
    driver = {
        throw IllegalStateException("TEST FAILED - Browser Is not needed for this test")
    }
}) {

    @Test
    fun `browser is not started when were not used`() {
        println(
            "Assert - When browser is not used, then no browser is initialized.\n" +
                    "If test try to create browser, exception will be thrown and this test fails."
        )
    }

    @Test
    fun `WebDriver is not started when driver is not used`() {
        val obtainedBrowser = browser
        println(
            "Assert - When driver is not used, then no WebDriver is initialized.\n" +
                    "If test try to create browser, exception will be thrown and this test fails."
        )
    }

}