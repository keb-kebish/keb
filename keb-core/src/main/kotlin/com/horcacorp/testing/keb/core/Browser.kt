package com.horcacorp.testing.keb.core

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

    val driver = config.driver ?: throw IllegalStateException("Browser driver not configured.")
    var baseUrl = config.baseUrl

    override val browser get() = this

    fun quit() {
        driver.quit()
    }

}