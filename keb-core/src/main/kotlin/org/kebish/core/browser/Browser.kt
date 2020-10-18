package org.kebish.core.browser

import org.kebish.core.ContentSupport
import org.kebish.core.ModuleSupport
import org.kebish.core.NavigationSupport
import org.kebish.core.WaitSupport
import org.kebish.core.config.Configuration
import org.kebish.core.util.ResettableLazy
import org.openqa.selenium.JavascriptException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.html5.WebStorage
import org.openqa.selenium.interactions.Actions


public class Browser(public var config: Configuration) : ContentSupport, NavigationSupport, ModuleSupport, WaitSupport {

    public companion object {

        public const val NO_PAGE_SOURCE_SUBSTITUTE: String = "-- no page source --"

        public fun drive(config: Configuration = Configuration(), block: Browser.() -> Unit) {
            val browser = Browser(config)
            try {
                browser.block()
            } finally {
                browser.quit()
            }
        }
    }

    private val driverDelegate = ResettableLazy(
        onReset = { driver -> driver.quit() },
        initializer = { config.driver() })
    public val driver: WebDriver by driverDelegate

    public var baseUrl: String
        get() = config.baseUrl
        set(value) {
            config.baseUrl = value
        }

    public var url: String
        get() = driver.currentUrl
        set(url) = driver.get(url) //TODO work with baseUrl - e.g. baseUrl=kebish.org, then  set("hello") will open "kebish.org/hello"

    public fun refresh(): Unit = driver.navigate().refresh()

    public val title: String
        get() = driver.title

    override val browser: Browser get() = this

    public fun quit() {
        driverDelegate.reset() // during this WebDriver.quit() is called
    }

    /** return browser.driver.pageSource or NO_PAGE_SOURCE_SUBSTITUTE in case it is null */
    public val pageSource: String
        get() = browser.driver.pageSource ?: NO_PAGE_SOURCE_SUBSTITUTE


    public fun clearCookies() {
        if (isDriverInitialized()) {
            driver.manage().deleteAllCookies()
        }
    }

    public fun clearCookiesQuietly() {
        try {
            clearCookies()
        } catch (e: WebDriverException) {
            // ignore
        }
    }

    public fun clearWebStorage() {
        val castDriver = driver
        if (castDriver is WebStorage) {
            try {
                castDriver.localStorage.clear()
                castDriver.sessionStorage.clear()
            } catch (e: JavascriptException) {
                // swallow this exception. It cannot be detected and is thrown, when no url is set in browser.
                // see test: org.kebish.core.browser.management.ClearWebStorage.clearWebStorageAfterEachTest on empty browser
            }
        }
    }

    public fun interact(block: Actions.() -> Unit) {
        Actions(driver)
            .apply(block)
            .build()
            .perform()
    }

    /**
     * Return true if browser hold reference to WebDriver.
     */
    public fun isDriverInitialized(): Boolean = driverDelegate.isInitialized()

}