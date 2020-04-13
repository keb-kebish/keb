package org.kebish.core.test

import org.kebish.core.*
import org.kebish.core.browser.provider.BrowserProvider
import org.kebish.core.browser.provider.StaticBrowserProvider
import org.openqa.selenium.JavascriptExecutor

/**
 * Implementation of keb test base
 */
abstract class KebTestBase(val config: Configuration) : ContentSupport, ModuleSupport, NavigationSupport, WaitSupport {

    private val browserProvider: BrowserProvider = StaticBrowserProvider(config)

    private val browserDelegate = lazy {
        browserProvider.provideBrowser()
    }

    override val browser: Browser by browserDelegate

    fun beforeEachTest() {
        if (browserDelegate.isInitialized() && config.browserManagement.closeBrowserBeforeAndAfterEachTest) {
            closeDriver()
        }
    }

    /** Test runner must call this method after each test */
    fun afterEachTest() {

        //TODO extract this to "browserDelegate.isInitialized()" - do not repeat it everywhere

        if (browserDelegate.isInitialized() && config.browserManagement.closeBrowserBeforeAndAfterEachTest) {
            closeDriver()
        } else {
            if (browserDelegate.isInitialized() && config.browserManagement.clearCookiesAfterEachTest) {
                browser.clearCookiesQuietly()
            }
            if (browserDelegate.isInitialized() && config.browserManagement.clearWebStorageAfterEachTest) {
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
            if (browserDelegate.isInitialized() && config.browserManagement.openNewEmptyWindowAndCloseOtherAfterEachTest) {
//                closeAllWindowsAndTabsExceptOne()
                openNewTabAndCloseOtherTabsAndWindows()
            }

            //TODO close forgotten dialog windows
        }

    }

//    private fun openNewTabAndCloseOtherTabsAndWindows() {
//        val windowHandles = browser.driver.windowHandles.toList()
//        if (windowHandles.size > 1) {
//            browser.driver.switchTo().window(windowHandles.first())
//            for (handle in windowHandles.subList(1, windowHandles.size)) {
//                browser.driver.close()
//                browser.driver.switchTo().window(handle)
//            }
//        }
//    }
//
//    private fun closeAllWindowsAndTabsExceptOne() {
//        val windowHandles = browser.driver.windowHandles.toList()
//        if (windowHandles.size > 1) {
//            browser.driver.switchTo().window(windowHandles.first())
//            for (handle in windowHandles.subList(1, windowHandles.size)) {
//                browser.driver.close()
//                browser.driver.switchTo().window(handle)
//            }
//        }
//    }


    private fun closeDriver() {
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
            //TODO only close windows
        }


        //        val windowHandles = browser.driver.windowHandles.toList()
        //        if (windowHandles.size > 1) {
        //            browser.driver.switchTo().window(windowHandles.first())
        //            for (handle in windowHandles.subList(1, windowHandles.size)) {
        //                browser.driver.close()
        //                browser.driver.switchTo().window(handle)
        //            }
        //        }
    }

    private fun closeTab(windowHandle: String) {
        browser.driver.switchTo().window(windowHandle)
        browser.driver.close() //TODO if window do not close, try close opened dialogs
        //        browser.driver.switchTo().alert().dismiss()
    }


}