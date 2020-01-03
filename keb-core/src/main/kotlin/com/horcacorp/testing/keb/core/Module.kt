package com.horcacorp.testing.keb.core

import org.openqa.selenium.WebElement

abstract class Module(val browser: Browser, val scope: WebElement? = null) : ContentSupport,
    NavigationSupport by browser, WaitSupport by browser, ModuleSupport by browser {

    override fun css(selector: String, scope: WebElement?) = cssSelector(selector, scope).getWebElement()

    override fun cssList(selector: String, scope: WebElement?) = cssSelector(selector, scope).getWebElements()

    override fun html(tag: String, scope: WebElement?) = htmlSelector(tag, scope).getWebElement()

    override fun htmlList(tag: String, scope: WebElement?) = htmlSelector(tag, scope).getWebElements()

    override fun xpath(xpath: String, scope: WebElement?) = xpathSelector(xpath, scope).getWebElement()

    override fun xpathList(xpath: String, scope: WebElement?) = xpathSelector(xpath, scope).getWebElements()
    
    private fun cssSelector(selector: String, scope: WebElement?) = when {
        scope != null -> ScopedCssSelector(selector, scope)
        this.scope != null -> ScopedCssSelector(selector, this.scope)
        else -> CssSelector(selector, browser.driver)
    }

    private fun htmlSelector(tag: String, scope: WebElement?) = when {
        scope != null -> ScopedHtmlSelector(tag, scope)
        this.scope != null -> ScopedHtmlSelector(tag, this.scope)
        else -> HtmlSelector(tag, browser.driver)
    }

    private fun xpathSelector(xpath: String, scope: WebElement?) = when {
        scope != null -> ScopedXpathSelector(xpath, scope)
        this.scope != null -> ScopedXpathSelector(xpath, this.scope)
        else -> XPathSelector(xpath, browser.driver)
    }
}