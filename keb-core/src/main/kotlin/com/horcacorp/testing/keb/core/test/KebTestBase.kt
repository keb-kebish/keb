package com.horcacorp.testing.keb.core.test

import com.horcacorp.testing.keb.core.Browser
import com.horcacorp.testing.keb.core.NavigationSupport
import com.horcacorp.testing.keb.core.Page

/**
 * Implementation of keb test base
 */
abstract class KebTestBase(val browser: Browser) : NavigationSupport {

    /** Test runner must call this method after each test */
    fun afterEachTest() {
        closeDriver()
    }

    private fun closeDriver() {
        browser.quit()
    }


    override fun <T : Page> to(pageFactory: () -> T, waitPreset: String?, body: T.() -> Unit) =
        browser.to(pageFactory, waitPreset, body)

    override fun <T : Page> to(page: T, waitPreset: String?, body: (T) -> Unit) =
        browser.to(page, waitPreset, body)

    override fun <T : Page> at(pageFactory: () -> T, waitPreset: String?, body: T.() -> Unit) =
        browser.at(pageFactory, waitPreset, body)

    override fun <T : Page> at(page: T, waitPreset: String?, body: (T) -> Unit) =
        browser.at(page, waitPreset, body)
}