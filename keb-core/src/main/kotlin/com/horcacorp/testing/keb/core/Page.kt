package com.horcacorp.testing.keb.core

abstract class Page(val browser: Browser) : ContentSupport by browser, NavigationSupport by browser,
    WaitSupport by browser, ModuleSupport by browser {

    open fun url(): String = ""
    open fun at(): Any = true

    fun verifyAt(waitPreset: String?) {
        waitFor(
            waitPreset, desc = "Verifying of ${javaClass.simpleName}", f = ::at
        )
    }

    fun Module.module() = browser.module(this)

}