package com.horcacorp.testing.keb.core

import org.openqa.selenium.WebElement
import kotlin.reflect.KProperty

class SingleWebElementDelegate(
    private val selector: Selector,
    private val fetchType: ContentFetchType,
    private val waitSupport: WaitSupport,
    private val wait: Any
) {
    private var _value: WebElement? = null
    private val value: WebElement
        get() = if (_value == null || fetchType == ContentFetchType.ON_EVERY_ACCESS) {
            _value = selector.getWebElement()
            _value!!
        } else {
            _value!!
        }

    override fun toString(): String = if (_value != null || fetchType == ContentFetchType.ON_EVERY_ACCESS) {
        value.toString()
    } else {
        "Web element not initialized yet."
    }

    fun text() = value.text

    operator fun getValue(thisRef: Any?, property: KProperty<*>) =
        waitSupport.waitFor(wait, "Page content by $selector") { value }

}

class WebElementsListDelegate(
    private val selector: Selector,
    private val fetchType: ContentFetchType,
    private val waitSupport: WaitSupport,
    private val wait: Any
) : Iterable<WebElement> {
    private var _value: List<WebElement>? = null
    private val value: List<WebElement>
        get() = if (_value == null || fetchType == ContentFetchType.ON_EVERY_ACCESS) {
            _value = selector.getWebElements()
            _value!!
        } else {
            _value!!
        }

    override fun toString() = if (_value != null || fetchType == ContentFetchType.ON_EVERY_ACCESS) {
        value.toString()
    } else {
        "List of web elements not initialized yet."
    }

    override fun iterator(): Iterator<WebElement> {
        return object : Iterator<WebElement> {
            private var index = 0
            override fun hasNext() = index < value.size

            override fun next() = value[index++]
        }
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>) =
        waitSupport.waitFor(wait, "Page content by $selector") { value }

}