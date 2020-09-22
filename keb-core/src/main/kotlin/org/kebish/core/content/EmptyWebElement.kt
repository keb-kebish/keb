package org.kebish.core.content

import org.openqa.selenium.*

class EmptyWebElement(override val missingContentSelector: String) : WebElement, EmptyContent {

    override fun isDisplayed(): Boolean {
        return false
    }

    override fun getLocation(): Point? {
        return null
    }

    override fun clear() {
        throw EmptyWebElementException("Web element by '$missingContentSelector' not present. Operation cannot be invoked.")
    }

    override fun submit() {
        throw EmptyWebElementException("Web element by '$missingContentSelector' not present. Operation cannot be invoked.")
    }

    override fun <X : Any?> getScreenshotAs(target: OutputType<X>?): X {
        throw EmptyWebElementException("Web element by '$missingContentSelector' not present. Operation cannot be invoked.")
    }

    override fun findElement(by: By?): WebElement {
        throw EmptyWebElementException("Web element by '$missingContentSelector' not present. Operation cannot be invoked.")
    }

    override fun click() {
        throw EmptyWebElementException("Web element by '$missingContentSelector' not present. Operation cannot be invoked.")
    }

    override fun getTagName(): String {
        throw EmptyWebElementException("Web element by '$missingContentSelector' not present. Operation cannot be invoked.")
    }

    override fun getSize(): Dimension {
        throw EmptyWebElementException("Web element by '$missingContentSelector' not present. Operation cannot be invoked.")
    }

    override fun getText(): String {
        throw EmptyWebElementException("Web element by '$missingContentSelector' not present. Operation cannot be invoked.")
    }

    override fun isSelected(): Boolean {
        throw EmptyWebElementException("Web element by '$missingContentSelector' not present. Operation cannot be invoked.")
    }

    override fun isEnabled(): Boolean {
        throw EmptyWebElementException("Web element by '$missingContentSelector' not present. Operation cannot be invoked.")
    }

    override fun sendKeys(vararg keysToSend: CharSequence?) {
        throw EmptyWebElementException("Web element by '$missingContentSelector' not present. Operation cannot be invoked.")
    }

    override fun getAttribute(name: String?): String {
        throw EmptyWebElementException("Web element by '$missingContentSelector' not present. Operation cannot be invoked.")
    }

    override fun getRect(): Rectangle {
        throw EmptyWebElementException("Web element by '$missingContentSelector' not present. Operation cannot be invoked.")
    }

    override fun getCssValue(propertyName: String?): String {
        throw EmptyWebElementException("Web element by '$missingContentSelector' not present. Operation cannot be invoked.")
    }

    override fun findElements(by: By?): MutableList<WebElement> {
        throw EmptyWebElementException("Web element by '$missingContentSelector' not present. Operation cannot be invoked.")
    }
}

class EmptyWebElementException(message: String) : RuntimeException(message)