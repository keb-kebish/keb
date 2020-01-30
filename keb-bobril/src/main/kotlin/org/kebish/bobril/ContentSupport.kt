package org.kebish.bobril

import org.kebish.core.Browser
import org.kebish.core.ContentSupport
import org.openqa.selenium.WebElement

fun ContentSupport.bb(selector: String, scope: WebElement? = getDefaultScope()) =
    bbSelector(browser, selector, scope).getWebElement()

fun ContentSupport.bbList(selector: String, scope: WebElement? = getDefaultScope()) =
    bbSelector(browser, selector, scope).getWebElements()

private fun bbSelector(browser: Browser, selector: String, scope: WebElement?) = when {
    scope != null -> ScopedBobrilSelector(selector, browser, scope)
    else -> BobrilSelector(selector, browser)
}