package org.kebish.core

import org.kebish.core.util.ResettableLazy
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.html5.WebStorage
import org.openqa.selenium.interactions.Actions


class Browser(var config: Configuration) : ContentSupport, NavigationSupport, ModuleSupport, WaitSupport {

    companion object {

        const val NO_PAGE_SOURCE_SUBSTITUTE: String = "-- no page source --"

        fun drive(config: Configuration = Configuration(), block: Browser.() -> Unit) {
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
    val driver by driverDelegate

    var baseUrl
        get() = config.baseUrl
        set(value) {
            config.baseUrl = value
        }


    var url: String
        get() = driver.currentUrl
        set(url) = driver.get(url) //TODO work with baseUrl - e.g. baseUrl=kebis.org, then  set("hello") will open "kebish.org/hello"

    fun refresh() = driver.navigate().refresh()

    override val browser get() = this

    fun quit() {
        driverDelegate.reset() // during this WebDriver.quit() is called
    }

    /** return browser.driver.pageSource or NO_PAGE_SOURCE_SUBSTITUTE in cas it is null */
    val pageSource: String
        get() = browser.driver.pageSource ?: NO_PAGE_SOURCE_SUBSTITUTE


    fun clearCookies() {
        if (isDriverInitialized()) {
            driver.manage().deleteAllCookies()
        }
    }

    fun clearCookiesQuietly() {
        try {
            clearCookies()
        } catch (e: WebDriverException) {
            // ignore
        }
    }

    fun clearWebStorage() {
        if (driver is WebStorage) {
            val driverCasted = driver as WebStorage
            driverCasted.localStorage.clear()
            driverCasted.sessionStorage.clear()
        } else {
            // TODO logger warn "WebStorage cannot be cleared. Driver is not instanceof "org.openqa.selenium.html5.WebStorage" "
        }
    }

    fun interact(block: Actions.() -> Unit) {
        Actions(driver)
            .apply(block)
            .build()
            .perform()
    }

    /**
     * Return true if browser hold reference to WebDriver.
     */
    fun isDriverInitialized(): Boolean = driverDelegate.isInitialized()

}