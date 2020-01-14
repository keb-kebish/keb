package org.kebish.core.test

import org.kebish.core.Browser
import org.kebish.core.NavigationSupport

/**
 * Implementation of keb test base
 */
abstract class KebTestBase(override val browser: Browser) : NavigationSupport {

    /** Test runner must call this method after each test */
    fun afterEachTest() {
        closeDriver()
    }

    private fun closeDriver() {
        browser.quit()
    }
}