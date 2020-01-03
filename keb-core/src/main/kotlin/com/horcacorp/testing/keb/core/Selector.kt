package com.horcacorp.testing.keb.core

import org.openqa.selenium.*

abstract class Selector(val selector: String) {
    fun getWebElement(): WebElement {
        val context = findElements()
        return when {
            context.isEmpty() -> throw NoSuchElementException("Required page content is not present. Selector='$selector'.")
            context.size > 1 -> throw TooManyElementsException(toString(), context.size)
            else -> context.first()
        }
    }

    fun getWebElements(): List<WebElement> {
        val context = findElements()
        return when {
            context.isEmpty() -> throw NoSuchElementException("Required page content is not present. Selector='$selector'.")
            else -> context
        }
    }

    protected abstract fun findElements(): List<WebElement>
}

class CssSelector(selector: String, private val searchContext: SearchContext) : Selector(selector) {
    override fun findElements(): List<WebElement> = searchContext.findElements(By.cssSelector(selector))
    override fun toString() = "CSS selector '$selector'"
}

class HtmlSelector(selector: String, private val searchContext: SearchContext) : Selector(selector) {
    override fun findElements(): List<WebElement> = searchContext.findElements(By.tagName(selector))
    override fun toString() = "HTML tag '$selector'"
}

class XPathSelector(selector: String, private val searchContext: SearchContext) : Selector(selector) {
    override fun findElements(): List<WebElement> = searchContext.findElements(By.xpath(selector))
    override fun toString() = "XPath '$selector'"
}

class TooManyElementsException(selectedBy: String, elementsSize: Int) :
    RuntimeException("Expecting only single element for $selectedBy, but got $elementsSize")