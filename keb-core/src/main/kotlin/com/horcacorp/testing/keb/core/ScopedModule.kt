package com.horcacorp.testing.keb.core

import org.openqa.selenium.WebElement

abstract class ScopedModule(val browser: Browser, private val scope: WebElement) : ContentSupport,
    NavigationSupport by browser, WaitSupport by browser, ModuleSupport by browser {

    override fun css(
        selector: String,
        scope: WebElement?
    ) = (scope?.let { ScopedCssSelector(selector, it) } ?: ScopedCssSelector(selector, this.scope)).getWebElement()

    override fun cssList(
        selector: String,
        scope: WebElement?
    ) = (scope?.let { ScopedCssSelector(selector, it) } ?: ScopedCssSelector(selector, this.scope)).getWebElements()

    override fun html(
        tag: String,
        scope: WebElement?
    ) = (scope?.let { ScopedHtmlSelector(tag, it) } ?: ScopedHtmlSelector(tag, this.scope)).getWebElement()

    override fun htmlList(
        tag: String,
        scope: WebElement?
    ) = (scope?.let { ScopedHtmlSelector(tag, it) } ?: ScopedHtmlSelector(tag, this.scope)).getWebElements()

    override fun xpath(
        xpath: String,
        scope: WebElement?
    ) = (scope?.let { ScopedXpathSelector(xpath, it) } ?: ScopedXpathSelector(xpath, this.scope)).getWebElement()

    override fun xpathList(
        xpath: String,
        scope: WebElement?
    ) = (scope?.let { ScopedXpathSelector(xpath, it) } ?: ScopedXpathSelector(xpath, this.scope)).getWebElements()

}