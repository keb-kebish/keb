package org.kebish.core

import org.openqa.selenium.By
import org.openqa.selenium.WebElement

interface ContentSupport {
    val browser: Browser
    fun getDefaultScope(): WebElement? = null
}

fun ContentSupport.css(selector: String, scope: WebElement? = getDefaultScope()) =
    cssSelector(browser, selector, scope).getWebElement()

fun ContentSupport.cssList(selector: String, scope: WebElement? = getDefaultScope()) =
    cssSelector(browser, selector, scope).getWebElements()

fun ContentSupport.html(selector: String, scope: WebElement? = getDefaultScope()) =
    htmlSelector(browser, selector, scope).getWebElement()

fun ContentSupport.htmlList(selector: String, scope: WebElement? = getDefaultScope()) =
    htmlSelector(browser, selector, scope).getWebElements()

fun ContentSupport.xpath(selector: String, scope: WebElement? = getDefaultScope()) =
    xpathSelector(browser, selector, scope).getWebElement()

fun ContentSupport.xpathList(selector: String, scope: WebElement? = getDefaultScope()) =
    xpathSelector(browser, selector, scope).getWebElements()

fun ContentSupport.by(selector: By, scope: WebElement? = getDefaultScope()) =
    bySelector(browser, selector, scope).getWebElement()

fun ContentSupport.byList(selector: By, scope: WebElement? = getDefaultScope()) =
    bySelector(browser, selector, scope).getWebElements()

private fun cssSelector(browser: Browser, selector: String, scope: WebElement?) =
    CssSelector(selector, scope ?: browser.driver)

private fun htmlSelector(browser: Browser, tag: String, scope: WebElement?) =
    HtmlSelector(tag, scope ?: browser.driver)

private fun xpathSelector(browser: Browser, xpath: String, scope: WebElement?) =
    XPathSelector(xpath, scope ?: browser.driver)

private fun bySelector(browser: Browser, by: By, scope: WebElement?) =
    BySelector(by, scope ?: browser.driver)