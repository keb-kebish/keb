package org.kebish.core.content

import org.openqa.selenium.By
import org.openqa.selenium.SearchContext
import org.openqa.selenium.WebElement

public abstract class Selector<T> {
    public abstract val selector: T
    public fun getWebElement(): WebElement {
        val context = findElements()
        return when {
            context.isEmpty() -> EmptyWebElement(selector.toString())
            context.size > 1 -> throw TooManyElementsException(toString(), context.size)
            else -> context.first()
        }
    }

    public fun getWebElements(): List<WebElement> {
        val context = findElements()
        return when {
            context.isEmpty() -> EmptyWebElementList(selector.toString())
            else -> context
        }
    }

    protected abstract fun findElements(): List<WebElement>
}

public class CssSelector(override val selector: String, private val searchContext: SearchContext) : Selector<String>() {
    override fun findElements(): List<WebElement> = searchContext.findElements(By.cssSelector(selector))
    override fun toString(): String = "CSS selector '$selector'"
}

public class HtmlSelector(override val selector: String, private val searchContext: SearchContext) :
    Selector<String>() {
    override fun findElements(): List<WebElement> = searchContext.findElements(By.tagName(selector))
    override fun toString(): String = "HTML tag '$selector'"
}

public class XPathSelector(override val selector: String, private val searchContext: SearchContext) :
    Selector<String>() {
    override fun findElements(): List<WebElement> = searchContext.findElements(By.xpath(selector))
    override fun toString(): String = "XPath '$selector'"
}

public class BySelector(override val selector: By, private val searchContext: SearchContext) : Selector<By>() {
    override fun findElements(): List<WebElement> = searchContext.findElements(selector)
    override fun toString(): String = "By '$selector'"
}

public class TooManyElementsException(selectedBy: String, elementsSize: Int) :
    RuntimeException("Expecting only single element for $selectedBy, but got $elementsSize")