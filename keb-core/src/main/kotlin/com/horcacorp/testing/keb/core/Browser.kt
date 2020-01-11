package com.horcacorp.testing.keb.core

import com.horcacorp.testing.keb.core.util.ResettableLazy

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

    private val driverDelegate = ResettableLazy(
        onReset = { driver -> driver.quit() },
        initializer = { config.driver() })
    val driver by driverDelegate

    var baseUrl = config.baseUrl

    override val browser get() = this

    fun quit() {
        driverDelegate.reset()
    }

}