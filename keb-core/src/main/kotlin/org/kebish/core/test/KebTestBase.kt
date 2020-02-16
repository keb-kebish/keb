package org.kebish.core.test

import org.kebish.core.*
import org.kebish.core.browser.provider.BrowserProvider
import org.kebish.core.browser.provider.StaticBrowserProvider

/**
 * Implementation of keb test base
 */
abstract class KebTestBase(val config: Configuration) : ContentSupport, ModuleSupport, NavigationSupport, WaitSupport {

    private val browserProvider: BrowserProvider = StaticBrowserProvider(config)

    override val browser: Browser
        get() = browserProvider.provideBrowser()

    /** Test runner must call this method after each test */
    fun afterEachTest() {
//        TODO calling this method depends on configuration
//        closeDriver()
    }

//    private fun closeDriver() {
//        browser.quit()
//    }
}