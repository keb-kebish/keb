package com.horcacorp.testing.keb.core.test

import com.horcacorp.testing.keb.core.Browser
import com.horcacorp.testing.keb.core.NavigationSupport
import com.horcacorp.testing.keb.core.Page

/**
 * Implementation of keb test base
 */
abstract class KebTestBase(val browser: Browser)  : NavigationSupport {

    /** Test runner must call this method after each test */
    fun afterEachTest() {
        closeDriver() //TODO By default - we don't want to close browser after each test
    }

    private fun closeDriver() {
        browser.quit()
    }

    // delegate navigation to browser
    override fun <T : Page> to(pageFactory: (Browser) -> T, waitPreset: String?, body: T.() -> Unit): T =
        browser.to(pageFactory, waitPreset, body)

    override fun <T : Page> at(pageFactory: (Browser) -> T, waitPreset: String?, body: T.() -> Unit): T =
        browser.at(pageFactory, waitPreset, body)

    override fun <T> withNewTab(action: () -> T): T = browser.withNewTab(action)
    override fun <T> withClosedTab(action: () -> T): T = browser.withClosedTab(action)

}