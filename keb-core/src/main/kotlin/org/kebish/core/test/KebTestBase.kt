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
            if (browserDelegate.isInitialized() && config.browserManagement.leftOnlyOneOpenedTab) {
                closeAllWindowsAndTabsExceptOne()
            }

            //TODO close forgotten dialog windows
        }

    }

    private fun closeAllWindowsAndTabsExceptOne() {
        val windowHandles = browser.driver.windowHandles.toList()
        if (windowHandles.size > 1) {
            browser.driver.switchTo().window(windowHandles.first())
            for (handle in windowHandles.subList(1, windowHandles.size)) {
                browser.driver.close()
                browser.driver.switchTo().window(handle)
            }
        }
    }


    private fun closeDriver() {
        browser.quit()
    }


}