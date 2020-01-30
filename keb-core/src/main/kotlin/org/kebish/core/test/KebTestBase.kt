package org.kebish.core.test

import org.kebish.core.*

/**
 * Implementation of keb test base
 */
abstract class KebTestBase(override val browser: Browser) : ContentSupport, ModuleSupport, NavigationSupport, WaitSupport {

    /** Test runner must call this method after each test */
    fun afterEachTest() {
        closeDriver()
    }

    private fun closeDriver() {
        browser.quit()
    }
}