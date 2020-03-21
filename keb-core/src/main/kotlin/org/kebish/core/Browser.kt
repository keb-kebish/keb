package org.kebish.core

import org.kebish.core.util.ResettableLazy
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.html5.WebStorage
import org.openqa.selenium.interactions.Actions

class Browser(val config: Configuration) : ContentSupport, NavigationSupport, ModuleSupport, WaitSupport {

    companion object {
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

    var baseUrl = config.baseUrl

    fun refresh() = driver.navigate().refresh()

    override val browser get() = this

    fun quit() {
        driverDelegate.reset()
    }

    fun clearCookies() {
        driver.manage().deleteAllCookies()
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

}