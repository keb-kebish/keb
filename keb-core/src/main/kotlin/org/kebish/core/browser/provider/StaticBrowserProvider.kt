package org.kebish.core.browser.provider

import org.kebish.core.Browser
import org.kebish.core.Configuration
import org.kebish.core.util.ResettableLazy
import org.openqa.selenium.JavascriptExecutor

class StaticBrowserProvider(
    //TODO document these options into README.md
    /** Cost approximately 15ms per test */
    var clearCookiesAfterEachTest: Boolean = true,
    /** Cost approximately 45ms per test */
    var clearWebStorageAfterEachTest: Boolean = false,
    /**
     *  Close all tabs and windows except one.
     *  If you close windows after test - you can be sure, that this test will not block execution of other tests.
     *  (e.g. by opening dialog windows on exit from page)
     *  */
    var openNewEmptyWindowAndCloseOtherAfterEachTest: Boolean = true
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

//        TODO use special BrowserProvider instead
//        if (browserDelegate.isInitialized() && config.browserManagement.closeBrowserBeforeAndAfterEachTest) {
//            closeDriver()
//        }
    }

    override fun afterTest() {


        //        TODO use special BrowserProvider instead
//        if (browserDelegate.isInitialized() && config.browserManagement.closeBrowserBeforeAndAfterEachTest) {
//            closeDriver()
//        } else {

        if (browserDelegate.isInitialized() && browser.isDriverInitialized()) {
            if (clearCookiesAfterEachTest) {
                browser.clearCookiesQuietly()
            }
            if (clearWebStorageAfterEachTest) {
                //                val driver = browser.driver
                //                if (driver is RemoteWebDriver) {
                //                    val sessionId = driver.sessionId
                //                    browser.clearWebStorage()
                //                } else {
                browser.clearWebStorage()
                //                }
                //                try {
                //                    browser.clearWebStorage()
                //                } catch (e: NoSuchSessionException) {
                //                    //TODO just log
                //                }
            }
            if (openNewEmptyWindowAndCloseOtherAfterEachTest) {
                //                closeAllWindowsAndTabsExceptOne()
                openNewTabAndCloseOtherTabsAndWindows()
            }
        }

        //TODO close forgotten dialog windows during closing windows


    }


    private fun closeDriver() {
        //TODO this should reset BrowserDelegate as well
        browser.quit()
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
        browser.driver.close() //TODO if window do not close, try close opened dialogs
        //        browser.driver.switchTo().alert().dismiss()
    }

}