package org.kebish.core

import org.kebish.core.util.ResettableLazy
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.html5.WebStorage

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
        val driverCasted = driver as WebStorage
        driverCasted.localStorage.clear()
        driverCasted.sessionStorage.clear()
    }

}