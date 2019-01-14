package com.horcacorp.testing.keb.core

abstract class Page(private val browser: Browser) : ContentSupport by browser, NavigationSupport by browser,
    WaitSupport by browser {

    open fun url(): String = ""
    open fun at(): Any = true

    fun verifyAt(waitParam: Any?) {
        waitFor(waitParam ?: browser.config.DEFAULT_WAIT_PRESET_NAME, desc = "${javaClass.simpleName} initialization", f = ::at)
    }

}