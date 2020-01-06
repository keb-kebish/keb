package com.horcacorp.testing.keb.core

import org.openqa.selenium.By
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

fun Page.by(selector: By, scope: WebElement? = null) =
    bySelector(browser, selector, scope).getWebElement()

fun Page.byList(selector: By, scope: WebElement? = null) =
    bySelector(browser, selector, scope).getWebElements()

fun Module.by(selector: By, scope: WebElement? = null) =
    (scope?.let { bySelector(browser, selector, it) } ?: bySelector(browser, selector, this.scope)).getWebElement()

fun Module.byList(selector: By, scope: WebElement? = null) =
    (scope?.let { bySelector(browser, selector, it) } ?: bySelector(browser, selector, this.scope)).getWebElements()

private fun cssSelector(browser: Browser, selector: String, scope: WebElement?) =
    CssSelector(selector, scope ?: browser.driver)

private fun htmlSelector(browser: Browser, tag: String, scope: WebElement?) =
    HtmlSelector(tag, scope ?: browser.driver)

private fun xpathSelector(browser: Browser, xpath: String, scope: WebElement?) =
    XPathSelector(xpath, scope ?: browser.driver)

private fun bySelector(browser: Browser, by: By, scope: WebElement?) =
    BySelector(by, scope ?: browser.driver)