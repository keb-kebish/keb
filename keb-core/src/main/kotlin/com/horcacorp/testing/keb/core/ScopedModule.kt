package com.horcacorp.testing.keb.core

import org.openqa.selenium.WebElement

abstract class ScopedModule(val browser: Browser, private val scope: WebElement) : ContentSupport,
    NavigationSupport by browser, WaitSupport by browser, ModuleSupport by browser {

    override fun css(
        selector: String,
        scope: WebElement?,
        fetch: ContentFetchType?
    ): WebElement = WebElementDelegate(
        scope?.let { ScopedCssSelector(selector, it) }
            ?: ScopedCssSelector(selector, this.scope),
        fetch ?: browser.config.elementsFetchType
    )

    override fun cssList(
        selector: String,
        scope: WebElement?,
        fetch: ContentFetchType?
    ): List<WebElement> = WebElementsListDelegate(
        scope?.let { ScopedCssSelector(selector, it) }
            ?: ScopedCssSelector(selector, this.scope),
        fetch ?: browser.config.elementsFetchType
    )

    override fun html(tag: String, scope: WebElement?, fetch: ContentFetchType?): WebElement =
        WebElementDelegate(
            scope?.let { ScopedHtmlSelector(tag, it) }
                ?: ScopedHtmlSelector(tag, this.scope),
            fetch ?: browser.config.elementsFetchType
        )

    override fun htmlList(
        tag: String,
        scope: WebElement?,
        fetch: ContentFetchType?
    ): List<WebElement> = WebElementsListDelegate(
        scope?.let { ScopedHtmlSelector(tag, it) }
            ?: ScopedHtmlSelector(tag, this.scope),
        fetch ?: browser.config.elementsFetchType
    )

    override fun xpath(
        xpath: String,
        scope: WebElement?,
        fetch: ContentFetchType?
    ): WebElement = WebElementDelegate(
        scope?.let { ScopedXpathSelector(xpath, it) }
            ?: ScopedXpathSelector(xpath, this.scope),
        fetch ?: browser.config.elementsFetchType
    )

    override fun xpathList(
        xpath: String,
        scope: WebElement?,
        fetch: ContentFetchType?
    ): List<WebElement> = WebElementsListDelegate(
        scope?.let { ScopedXpathSelector(xpath, it) }
            ?: ScopedXpathSelector(xpath, this.scope),
        fetch ?: browser.config.elementsFetchType
    )

}