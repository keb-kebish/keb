package org.kebish.core.test

import org.kebish.core.*
import org.kebish.core.browser.provider.BrowserProvider
import org.kebish.core.browser.provider.StaticBrowserProvider

/**
 * Implementation of keb test base
 */
abstract class KebTestBase(val config: Configuration) : ContentSupport, ModuleSupport, NavigationSupport, WaitSupport {

    private val browserProvider: BrowserProvider = StaticBrowserProvider(config)

    private val browserDelegate = lazy {
        browserProvider.provideBrowser()
    }

    override val browser: Browser by browserDelegate

    /** Test runner must call this method after each test */
    fun afterEachTest() {

        if (browserDelegate.isInitialized() && config.browserManagement.closeBrowserAfterEachTest) {
            closeDriver()
        } else {
            if (browserDelegate.isInitialized() && config.browserManagement.clearCookiesAfterEachTest) {
                browser.clearCookiesQuietly()
            }
            if (browserDelegate.isInitialized() && config.browserManagement.clearWebStorageAfterEachTest) {
                browser.clearWebStorage()
            }
            //TODO close all windows except one

            //TODO close all tabs except one

            //TODO close forgotten dialog windows
        }

    }

    private fun closeDriver() {
        browser.quit()
    }


}