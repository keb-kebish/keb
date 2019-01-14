package com.horcacorp.testing.keb.core

import org.openqa.selenium.WebElement
import kotlin.reflect.KProperty

class ModuleDelegate<T>(factory: (Browser) -> T, browser: Browser) {
    private val value = factory(browser)
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = value
    override fun toString() = value.toString()
}

class ScopedModuleDelegate<T>(
    private val scope: Selector,
    private val factory: (Browser, WebElement) -> T,
    private val fetchType: ContentFetchType,
    private val browser: Browser,
    private val wait: Any
) {
    private var _value: T? = null
    private val value: T
        get() = if (_value == null || fetchType == ContentFetchType.ON_EVERY_ACCESS) {
            _value = factory(browser, scope.getWebElement())
            _value!!
        } else {
            _value!!
        }

    override fun toString(): String = if (_value != null || fetchType == ContentFetchType.ON_EVERY_ACCESS) {
        value.toString()
    } else {
        "Web element not initialized yet."
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>) =
        browser.waitFor(wait, "Module scope by '$scope'") { value }

}