package org.kebish.core

import org.openqa.selenium.*

abstract class Selector<T> {
    abstract val selector: T
    fun getWebElement(): WebElement {
        val context = findElements()
        return when {
            context.isEmpty() -> EmptyWebElement(selector.toString())
            context.size > 1 -> throw TooManyElementsException(toString(), context.size)
            else -> context.first()
        }
    }

    fun getWebElements(): List<WebElement> {
        val context = findElements()
        return when {
            context.isEmpty() -> EmptyWebElementList(selector.toString())
            else -> context
        }
    }

    protected abstract fun findElements(): List<WebElement>
}

class CssSelector(override val selector: String, private val searchContext: SearchContext) : Selector<String>() {
    override fun findElements(): List<WebElement> = searchContext.findElements(By.cssSelector(selector))
    override fun toString() = "CSS selector '$selector'"
}

class HtmlSelector(override val selector: String, private val searchContext: SearchContext) : Selector<String>() {
    override fun findElements(): List<WebElement> = searchContext.findElements(By.tagName(selector))
    override fun toString() = "HTML tag '$selector'"
}

class XPathSelector(override val selector: String, private val searchContext: SearchContext) : Selector<String>() {
    override fun findElements(): List<WebElement> = searchContext.findElements(By.xpath(selector))
    override fun toString() = "XPath '$selector'"
}

class BySelector(override val selector: By, private val searchContext: SearchContext) : Selector<By>() {
    override fun findElements(): List<WebElement> = searchContext.findElements(selector)
    override fun toString() = "By '$selector'"
}

class TooManyElementsException(selectedBy: String, elementsSize: Int) :
    RuntimeException("Expecting only single element for $selectedBy, but got $elementsSize")