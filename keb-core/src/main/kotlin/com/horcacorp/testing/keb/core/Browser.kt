package com.horcacorp.testing.keb.core

import org.openqa.selenium.WebDriver

class Browser(val config: Configuration) : NavigationSupport, ModuleSupport, WaitSupport {

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

    private var driverHolder: WebDriver? = null

    val driver: WebDriver
        get() {
            if (driverHolder == null) {
                driverHolder = config.driver()
            }
            return driverHolder as WebDriver
        }

    var baseUrl = config.baseUrl

    override val browser get() = this

    fun quit() {
        val closingDriver = driverHolder
        driverHolder = null
        closingDriver?.quit()
    }

}