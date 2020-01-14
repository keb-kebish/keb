package org.kebish.bobril

import org.kebish.core.Browser
import org.kebish.core.Selector
import org.openqa.selenium.WebElement

class BobrilSelector(override val selector: String, private val browser: Browser) : Selector<String>() {
    override fun findElements() = BbSeeker.findElements(selector, browser.driver)
    override fun toString() = "Bobril selector '$selector'"
}

class ScopedBobrilSelector(
    override val selector: String,
    private val browser: Browser,
    private val scope: WebElement
) : Selector<String>() {
    override fun findElements() = BbSeeker.findElements(selector, browser.driver, scope)
    override fun toString() = "Bobril selector '$selector' in context of '$scope'"
}