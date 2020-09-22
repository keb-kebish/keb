package org.kebish.core.browser.provider

import org.kebish.core.browser.Browser
import org.kebish.core.config.Configuration
import org.kebish.core.util.ResettableLazy
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.NoAlertPresentException
import org.openqa.selenium.UnhandledAlertException

class StaticBrowserProvider(
    /** Cost approximately 15ms per test */
    var clearCookiesAfterEachTest: Boolean = true,
    /** Cost approximately 45ms per test */
    var clearWebStorageAfterEachTest: Boolean = false,
    /**
     *  Close all tabs and windows except one.
     *  If you close windows after test - you can be sure, that this test will not block execution of other tests.
     *  (e.g. by opening dialog windows on exit from page)
     *  */
    var openNewEmptyWindowAndCloseOtherAfterEachTest: Boolean = true,
    /**
     * If window have opened alert dialog, which prevent window to close. This will try to close alert.
     */
    var autoCloseAlerts: Boolean = true
) : BrowserProvider {


    companion object {

        private lateinit var config: Configuration

        private val browserDelegate = ResettableLazy(
            initializer = {
                val browser = Browser(config)
                //There is no need to have extra thread for each browser
                //But there is no issue in StaticBrowser provider, because there is only one all the time
                //With multiple browsers consider - static WeakReference Set of browsers and only one shutdown hook
                Runtime.getRuntime().addShutdownHook(object : Thread() {
                    override fun run() {
                        browser.quit()
                    }
                })
                browser
            },
            onReset = { browser -> browser.quit() }
        )

        private val browser by browserDelegate

        /**
         * Close browser and create new one on next call of provideDriver() method.
         */
        fun reset() {
            browserDelegate.reset()
        }

    }

    override fun provideBrowser(config: Configuration): Browser {
        StaticBrowserProvider.config = config
        browser.config = config
        return browser
    }

    override fun beforeTest() {
    }

    override fun afterTest() {
        if (browserDelegate.isInitialized() && browser.isDriverInitialized()) {
            if (clearCookiesAfterEachTest) {
                browser.clearCookiesQuietly()
            }
            if (clearWebStorageAfterEachTest) {
                browser.clearWebStorage()
            }
            if (openNewEmptyWindowAndCloseOtherAfterEachTest) {
                openNewTabAndCloseOtherTabsAndWindows()
            }
        }
    }

    private fun openNewTabAndCloseOtherTabsAndWindows() {

        val originalHandles = browser.driver.windowHandles
        val driver = browser.driver

        if (driver is JavascriptExecutor) {
            driver.executeScript("window.open();")
            val newHandles = browser.driver.windowHandles
            val difference = newHandles.minus(originalHandles)
            if (difference.size != 1) {
                throw IllegalStateException("Diff - $difference, orig - $originalHandles, new - $newHandles ")
            }
            val newEmptyWindow = difference.first()
            val windowsToClose = newHandles.minus(newEmptyWindow)
            windowsToClose.forEach { closeTab(it) }

            driver.switchTo().window(newEmptyWindow)
        } else {
            val iterator = browser.driver.windowHandles.iterator()
            if (iterator.hasNext()) {
                val handle = iterator.next()
                iterator.forEachRemaining { closeTab(it) }

                driver.switchTo().window(handle)
            }
        }

    }

    private fun closeTab(windowHandle: String) {
        browser.driver.switchTo().window(windowHandle)
        try {
            browser.driver.close()
        } catch (e: UnhandledAlertException) {
            if (autoCloseAlerts) {
                try {
                    browser.driver.switchTo().alert().dismiss()
                } catch (e: NoAlertPresentException) {
                    // close may dismiss dialog - so this is possible
                }
                browser.driver.close()
            }
        }

    }

}