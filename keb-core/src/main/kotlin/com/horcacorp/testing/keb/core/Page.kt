package com.horcacorp.testing.keb.core

abstract class Page : NavigationSupport, ModuleSupport, WaitSupport {

    override lateinit var browser: Browser

    open fun url(): String = ""
    open fun at(): Any = true

    fun verifyAt(waitPreset: String?) {
        waitFor(
            waitPreset, desc = "Verifying of ${javaClass.simpleName}", f = ::at
        )
    }
}