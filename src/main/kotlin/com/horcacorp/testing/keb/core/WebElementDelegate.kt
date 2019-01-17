package com.horcacorp.testing.keb.core

import org.openqa.selenium.*
import kotlin.reflect.KProperty

enum class ContentFetchType {
    ON_EVERY_ACCESS, ON_FIRST_ACCESS
}

class WebElementDelegate(
    private val selector: Selector,
    private val fetchType: ContentFetchType,
    private val waitSupport: WaitSupport,
    private val wait: Any
) : WebElement {
    private var _value: WebElement? = null
    private val delegate: WebElement
        get() = if (_value == null || fetchType == ContentFetchType.ON_EVERY_ACCESS) {
            _value = selector.getWebElement()
            _value!!
        } else {
            _value!!
        }

    operator fun getValue(thisRef: Any?, property: KProperty<*>) =
        waitSupport.waitFor(wait, "Page content by $selector") { delegate }

    override fun toString() =
        if (_value != null || fetchType == ContentFetchType.ON_EVERY_ACCESS) "Web element by $selector ($delegate)."
        else "Web element not initialized yet."

    override fun isDisplayed(): Boolean = delegate.isDisplayed
    override fun clear() = delegate.clear()
    override fun submit() = delegate.submit()
    override fun getLocation(): Point = delegate.location
    override fun findElement(by: By): WebElement = delegate.findElement(by)
    override fun click() = delegate.click()
    override fun getTagName(): String = delegate.tagName
    override fun getSize(): Dimension = delegate.size
    override fun getText(): String = delegate.text
    override fun isSelected(): Boolean = delegate.isSelected
    override fun isEnabled(): Boolean = delegate.isEnabled
    override fun sendKeys(vararg keysToSend: CharSequence) = delegate.sendKeys(*keysToSend)
    override fun getAttribute(name: String): String = delegate.getAttribute(name)
    override fun getRect(): Rectangle = delegate.rect
    override fun getCssValue(propertyName: String): String = delegate.getCssValue(propertyName)
    override fun findElements(by: By): List<WebElement> = delegate.findElements(by)
    override fun <X : Any> getScreenshotAs(target: OutputType<X>): X = delegate.getScreenshotAs(target)
}

class WebElementsListDelegate(
    private val selector: Selector,
    private val fetchType: ContentFetchType,
    private val waitSupport: WaitSupport,
    private val wait: Any
) : List<WebElement> {
    private var _value: List<WebElement>? = null
    private val delegate: List<WebElement>
        get() = if (_value == null || fetchType == ContentFetchType.ON_EVERY_ACCESS) {
            _value = selector.getWebElements()
            _value!!
        } else {
            _value!!
        }

    operator fun getValue(thisRef: Any?, property: KProperty<*>) =
        waitSupport.waitFor(wait, "Page content by $selector") { delegate }

    override fun toString() =
        if (_value != null || fetchType == ContentFetchType.ON_EVERY_ACCESS) "List of web elements by $selector ($delegate)."
        else "List of web elements not initialized yet."

    override val size: Int get() = delegate.size
    override fun contains(element: WebElement): Boolean = delegate.contains(element)
    override fun containsAll(elements: Collection<WebElement>): Boolean = delegate.containsAll(elements)
    override fun get(index: Int): WebElement = delegate[index]
    override fun indexOf(element: WebElement): Int = delegate.indexOf(element)
    override fun isEmpty(): Boolean = delegate.isEmpty()
    override fun iterator(): Iterator<WebElement> = delegate.iterator()
    override fun lastIndexOf(element: WebElement): Int = delegate.lastIndexOf(element)
    override fun listIterator(): ListIterator<WebElement> = delegate.listIterator()
    override fun listIterator(index: Int): ListIterator<WebElement> = delegate.listIterator(index)
    override fun subList(fromIndex: Int, toIndex: Int): List<WebElement> = delegate.subList(fromIndex, toIndex)
}