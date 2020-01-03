package com.horcacorp.testing.keb.core.test

import com.horcacorp.testing.keb.core.Browser
import com.horcacorp.testing.keb.core.NavigationSupport
import com.horcacorp.testing.keb.core.Page

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