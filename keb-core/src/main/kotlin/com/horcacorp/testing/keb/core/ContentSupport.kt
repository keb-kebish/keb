package com.horcacorp.testing.keb.core

import org.openqa.selenium.WebElement

fun Page.css(selector: String, scope: WebElement? = null) =
    cssSelector(browser, selector, scope).getWebElement()

fun Page.cssList(selector: String, scope: WebElement? = null) =
    cssSelector(browser, selector, scope).getWebElements()

fun Module.css(selector: String, scope: WebElement? = null) =
    (scope?.let { cssSelector(browser, selector, it) } ?: cssSelector(browser, selector, this.scope)).getWebElement()

fun Module.cssList(selector: String, scope: WebElement? = null) =
    (scope?.let { cssSelector(browser, selector, it) } ?: cssSelector(browser, selector, this.scope)).getWebElements()

fun Page.html(selector: String, scope: WebElement? = null) =
    htmlSelector(browser, selector, scope).getWebElement()

fun Page.htmlList(selector: String, scope: WebElement? = null) =
    htmlSelector(browser, selector, scope).getWebElements()

fun Module.html(selector: String, scope: WebElement? = null) =
    (scope?.let { htmlSelector(browser, selector, it) } ?: htmlSelector(browser, selector, this.scope)).getWebElement()

fun Module.htmlList(selector: String, scope: WebElement? = null) =
    (scope?.let { htmlSelector(browser, selector, it) } ?: htmlSelector(browser, selector, this.scope)).getWebElements()

fun Page.xpath(selector: String, scope: WebElement? = null) =
    xpathSelector(browser, selector, scope).getWebElement()

fun Page.xpathList(selector: String, scope: WebElement? = null) =
    xpathSelector(browser, selector, scope).getWebElements()

fun Module.xpath(selector: String, scope: WebElement? = null) =
    (scope?.let { xpathSelector(browser, selector, it) } ?: xpathSelector(browser, selector, this.scope)).getWebElement()

fun Module.xpathList(selector: String, scope: WebElement? = null) =
    (scope?.let { xpathSelector(browser, selector, it) } ?: xpathSelector(browser, selector, this.scope)).getWebElements()

private fun cssSelector(browser: Browser, selector: String, scope: WebElement?) = when {
    scope != null -> ScopedCssSelector(selector, scope)
    else -> CssSelector(selector, browser.driver)
}

private fun htmlSelector(browser: Browser, tag: String, scope: WebElement?) = when {
    scope != null -> ScopedHtmlSelector(tag, scope)
    else -> HtmlSelector(tag, browser.driver)
}

private fun xpathSelector(browser: Browser, xpath: String, scope: WebElement?) = when {
    scope != null -> ScopedXpathSelector(xpath, scope)
    else -> XPathSelector(xpath, browser.driver)
}