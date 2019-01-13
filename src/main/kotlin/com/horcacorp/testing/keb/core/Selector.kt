package com.horcacorp.testing.keb.core

import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

interface Selector {
    fun getWebElement(): WebElement
    fun getWebElements(): List<WebElement>
}

class CssSelector(private val selector: String, private val driver: WebDriver) : Selector {
    override fun getWebElement(): WebElement {
        val context = driver.findElements(By.cssSelector(selector))
        return when {
            context.isEmpty() -> throw NoSuchElementException("Required page content by $selector is not present.")
            context.size > 1 -> throw TooManyElementsException(toString(), context.size)
            else -> context.first()
        }
    }

    override fun getWebElements(): List<WebElement> {
        val context = driver.findElements(By.cssSelector(selector))
        return when {
            context.isEmpty() -> throw NoSuchElementException("Required page content by $selector is not present.")
            else -> context
        }
    }

    override fun toString() = "CSS selector '$selector'"
}

class ScopedCssSelector(private val selector: String, private val scope: WebElement) : Selector {
    override fun getWebElement(): WebElement {
        val context = scope.findElements(By.cssSelector(selector))
        return when {
            context.isEmpty() -> throw NoSuchElementException("Required page content by $selector is not present.")
            context.size > 1 -> throw TooManyElementsException(toString(), context.size)
            else -> context.first()
        }
    }

    override fun getWebElements(): List<WebElement> {
        val context = scope.findElements(By.cssSelector(selector))
        return when {
            context.isEmpty() -> throw NoSuchElementException("Required page content by $selector is not present.")
            else -> context
        }
    }

    override fun toString() = "CSS selector '$selector' in context of '$scope"
}

class HtmlSelector(private val tag: String, private val driver: WebDriver) : Selector {
    override fun getWebElement(): WebElement {
        val context = driver.findElements(By.tagName(tag))
        return when {
            context.isEmpty() -> throw NoSuchElementException("Required page content by $tag is not present.")
            context.size > 1 -> throw TooManyElementsException(toString(), context.size)
            else -> context.first()
        }
    }

    override fun getWebElements(): List<WebElement> {
        val context = driver.findElements(By.tagName(tag))
        return when {
            context.isEmpty() -> throw NoSuchElementException("Required page content by $tag is not present.")
            else -> context
        }
    }

    override fun toString() = "HTML tag '$tag'"
}

class ScopedHtmlSelector(private val tag: String, private val scope: WebElement): Selector {
    override fun getWebElement(): WebElement {
        val context = scope.findElements(By.tagName(tag))
        return when {
            context.isEmpty() -> throw NoSuchElementException("Required page content by $tag is not present.")
            context.size > 1 -> throw TooManyElementsException(toString(), context.size)
            else -> context.first()
        }
    }

    override fun getWebElements(): List<WebElement> {
        val context = scope.findElements(By.tagName(tag))
        return when {
            context.isEmpty() -> throw NoSuchElementException("Required page content by $tag is not present.")
            else -> context
        }
    }

    override fun toString() = "HTML tag '$tag' in context of '$scope'"
}

class XPathSelector(private val xpath: String, private val driver: WebDriver) : Selector {
    override fun getWebElement(): WebElement {
        val context = driver.findElements(By.xpath(xpath))
        return when {
            context.isEmpty() -> throw NoSuchElementException("Required page content by $xpath is not present.")
            context.size > 1 -> throw TooManyElementsException(toString(), context.size)
            else -> context.first()
        }
    }

    override fun getWebElements(): List<WebElement> {
        val context = driver.findElements(By.xpath(xpath))
        return when {
            context.isEmpty() -> throw NoSuchElementException("Required page content by $xpath is not present.")
            else -> context
        }
    }

    override fun toString() = "XPath '$xpath'"
}

class ScopedXpathSelector(private val xpath: String, private val scope: WebElement): Selector {
    override fun getWebElement(): WebElement {
        val context = scope.findElements(By.xpath(xpath))
        return when {
            context.isEmpty() -> throw NoSuchElementException("Required page content by $xpath is not present.")
            context.size > 1 -> throw TooManyElementsException(toString(), context.size)
            else -> context.first()
        }
    }

    override fun getWebElements(): List<WebElement> {
        val context = scope.findElements(By.xpath(xpath))
        return when {
            context.isEmpty() -> throw NoSuchElementException("Required page content by $xpath is not present.")
            else -> context
        }
    }

    override fun toString() = "XPath '$xpath' in context of '$scope'"
}

class TooManyElementsException(selectedBy: String, elementsSize: Int) :
    RuntimeException("Expecting only single element for $selectedBy, but got $elementsSize")