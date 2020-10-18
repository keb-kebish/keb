package org.kebish.bobril

import org.kebish.core.browser.Browser
import org.kebish.core.content.Selector
import org.openqa.selenium.WebElement

public class BobrilSelector(override val selector: String, private val browser: Browser) : Selector<String>() {
    override fun findElements(): List<WebElement> = BbSeeker.findElements(selector, browser.driver)
    override fun toString(): String = "Bobril selector '$selector'"
}

public class ScopedBobrilSelector(
    override val selector: String,
    private val browser: Browser,
    private val scope: WebElement
) : Selector<String>() {
    override fun findElements(): List<WebElement> = BbSeeker.findElements(selector, browser.driver, scope)
    override fun toString(): String = "Bobril selector '$selector' in context of '$scope'"
}