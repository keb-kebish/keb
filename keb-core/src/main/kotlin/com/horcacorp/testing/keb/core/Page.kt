package com.horcacorp.testing.keb.core

import org.openqa.selenium.WebElement

abstract class Page : NavigationSupport, WaitSupport, ModuleSupport {

    lateinit var browser: Browser

    open fun url(): String = ""
    open fun at(): Any = true

    fun verifyAt(waitPreset: String?) {
        waitFor(
            waitPreset, desc = "Verifying of ${javaClass.simpleName}", f = ::at
        )
    }

    override fun <T : Page> to(pageFactory: () -> T, waitPreset: String?, body: (T) -> Unit) =
        browser.to(pageFactory, waitPreset, body)

    override fun <T : Page> to(page: T, waitPreset: String?, body: (T) -> Unit) =
        browser.to(page, waitPreset, body)

    override fun <T : Page> at(pageFactory: () -> T, waitPreset: String?, body: (T) -> Unit) =
        browser.at(pageFactory, waitPreset, body)

    override fun <T : Page> at(page: T, waitPreset: String?, body: (T) -> Unit) =
        browser.at(page, waitPreset, body)

    override fun <T> waitFor(presetName: String?, desc: String?, f: () -> T) =
        browser.waitFor(presetName, desc, f)

    override fun <T> waitFor(timeoutMillis: Long, retryIntervalMillis: Long, desc: String?, f: () -> T) =
        browser.waitFor(timeoutMillis, retryIntervalMillis, desc, f)

    override fun <T : Module> module(factory: (Browser, WebElement?) -> T, scope: WebElement?) =
        browser.module(factory, scope)
}