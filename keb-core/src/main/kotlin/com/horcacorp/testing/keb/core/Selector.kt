package com.horcacorp.testing.keb.core

import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

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

class CssSelector(selector: String, private val driver: WebDriver) : Selector(selector) {
    override fun findElements(): List<WebElement> = driver.findElements(By.cssSelector(selector))
    override fun toString() = "CSS selector '$selector'"
}

class ScopedCssSelector(selector: String, private val scope: WebElement) : Selector(selector) {
    override fun findElements(): List<WebElement> = scope.findElements(By.cssSelector(selector))
    override fun toString() = "CSS selector '$selector' in context of '$scope"
}

class HtmlSelector(selector: String, private val driver: WebDriver) : Selector(selector) {
    override fun findElements(): List<WebElement> = driver.findElements(By.tagName(selector))
    override fun toString() = "HTML tag '$selector'"
}

class ScopedHtmlSelector(selector: String, private val scope: WebElement) : Selector(selector) {
    override fun findElements(): List<WebElement> = scope.findElements(By.tagName(selector))
    override fun toString() = "HTML tag '$selector' in context of '$scope'"
}

class XPathSelector(selector: String, private val driver: WebDriver) : Selector(selector) {
    override fun findElements(): List<WebElement> = driver.findElements(By.xpath(selector))
    override fun toString() = "XPath '$selector'"
}

class ScopedXpathSelector(selector: String, private val scope: WebElement) : Selector(selector) {
    override fun findElements(): List<WebElement> = scope.findElements(By.xpath(selector))
    override fun toString() = "XPath '$selector' in context of '$scope'"
}

class TooManyElementsException(selectedBy: String, elementsSize: Int) :
    RuntimeException("Expecting only single element for $selectedBy, but got $elementsSize")