package org.kebish.bobril

import org.kebish.core.ContentSupport
import org.kebish.core.browser.Browser
import org.openqa.selenium.WebElement

public fun ContentSupport.bb(selector: String, scope: WebElement? = getDefaultScope()): WebElement =
    bbSelector(browser, selector, scope).getWebElement()

public fun ContentSupport.bbList(selector: String, scope: WebElement? = getDefaultScope()): List<WebElement> =
    bbSelector(browser, selector, scope).getWebElements()

private fun bbSelector(browser: Browser, selector: String, scope: WebElement?) = when {
    scope != null -> ScopedBobrilSelector(selector, browser, scope)
    else -> BobrilSelector(selector, browser)
}