package org.kebish.bobril

import org.kebish.core.Browser
import org.kebish.core.Module
import org.kebish.core.Page
import org.openqa.selenium.WebElement

fun Page.bb(selector: String, scope: WebElement? = null) =
    bbSelector(browser, selector, scope).getWebElement()

fun Page.bbList(selector: String, scope: WebElement? = null) =
    bbSelector(browser, selector, scope).getWebElements()

fun Module.bb(selector: String, scope: WebElement? = null) =
    (scope?.let { bbSelector(browser, selector, it) } ?: bbSelector(browser, selector, this.scope)).getWebElement()

fun Module.bbList(selector: String, scope: WebElement? = null) =
    (scope?.let { bbSelector(browser, selector, it) } ?: bbSelector(browser, selector, this.scope)).getWebElements()

private fun bbSelector(browser: Browser, selector: String, scope: WebElement?) = when {
    scope != null -> ScopedBobrilSelector(selector, browser, scope)
    else -> BobrilSelector(selector, browser)
}