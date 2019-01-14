package com.horcacorp.testing.keb.core

import org.openqa.selenium.WebElement

abstract class ScopedModule(private val browser: Browser, private val scope: WebElement) : ContentSupport,
    NavigationSupport by browser, WaitSupport by browser {

    override fun css(selector: String, fetch: ContentFetchType?, waitParam: Any?) =
        SingleWebElementDelegate(
            ScopedCssSelector(selector, scope),
            fetch ?: browser.config.elementsFetchType,
            browser,
            waitParam ?: false
        )

    override fun cssSelector(query: String) = ScopedCssSelector(query, scope)
    override fun css(
        selector: String,
        scope: WebElement,
        fetch: ContentFetchType?,
        waitParam: Any?
    ) = SingleWebElementDelegate(
        ScopedCssSelector(selector, scope),
        fetch ?: browser.config.elementsFetchType,
        browser,
        waitParam ?: false
    )

    override fun css(
        selector: String,
        scopeDelegate: SingleWebElementDelegate,
        fetch: ContentFetchType?,
        waitParam: Any?
    ): SingleWebElementDelegate {
        val scope by scopeDelegate
        return SingleWebElementDelegate(
            ScopedCssSelector(selector, scope),
            fetch ?: browser.config.elementsFetchType,
            browser,
            waitParam ?: false
        )
    }

    override fun cssList(selector: String, fetch: ContentFetchType?, waitParam: Any?) =
        WebElementsListDelegate(
            ScopedCssSelector(selector, scope),
            fetch ?: browser.config.elementsFetchType,
            browser,
            waitParam ?: false
        )

    override fun cssList(
        selector: String,
        scope: WebElement,
        fetch: ContentFetchType?,
        waitParam: Any?
    ) = WebElementsListDelegate(
        ScopedCssSelector(selector, scope),
        fetch ?: browser.config.elementsFetchType,
        browser,
        waitParam ?: false
    )

    override fun cssList(
        selector: String,
        scopeDelegate: SingleWebElementDelegate,
        fetch: ContentFetchType?,
        waitParam: Any?
    ): WebElementsListDelegate {
        val scope by scopeDelegate
        return WebElementsListDelegate(
            ScopedCssSelector(selector, scope),
            fetch ?: browser.config.elementsFetchType,
            browser,
            waitParam ?: false
        )
    }

    override fun htmlSelector(query: String) = ScopedHtmlSelector(query, scope)

    override fun html(tag: String, fetch: ContentFetchType?, waitParam: Any?) =
        SingleWebElementDelegate(
            ScopedHtmlSelector(tag, scope),
            fetch ?: browser.config.elementsFetchType,
            browser,
            waitParam ?: false
        )

    override fun html(
        tag: String,
        scope: WebElement,
        fetch: ContentFetchType?,
        waitParam: Any?
    ) = SingleWebElementDelegate(
        ScopedHtmlSelector(tag, scope),
        fetch ?: browser.config.elementsFetchType,
        browser,
        waitParam ?: false
    )

    override fun html(
        tag: String,
        scopeDelegate: SingleWebElementDelegate,
        fetch: ContentFetchType?,
        waitParam: Any?
    ): SingleWebElementDelegate {
        val scope by scopeDelegate
        return SingleWebElementDelegate(
            ScopedHtmlSelector(tag, scope),
            fetch ?: browser.config.elementsFetchType,
            browser,
            waitParam ?: false
        )
    }

    override fun htmlList(tag: String, fetch: ContentFetchType?, waitParam: Any?) =
        WebElementsListDelegate(
            ScopedHtmlSelector(tag, scope),
            fetch ?: browser.config.elementsFetchType,
            browser,
            waitParam ?: false
        )

    override fun htmlList(
        tag: String,
        scope: WebElement,
        fetch: ContentFetchType?,
        waitParam: Any?
    ) = WebElementsListDelegate(
        ScopedHtmlSelector(tag, scope),
        fetch ?: browser.config.elementsFetchType,
        browser,
        waitParam ?: false
    )

    override fun htmlList(
        tag: String,
        scopeDelegate: SingleWebElementDelegate,
        fetch: ContentFetchType?,
        waitParam: Any?
    ): WebElementsListDelegate {
        val scope by scopeDelegate
        return WebElementsListDelegate(
            ScopedHtmlSelector(tag, scope),
            fetch ?: browser.config.elementsFetchType,
            browser,
            waitParam ?: false
        )
    }

    override fun xpathSelector(query: String) = ScopedHtmlSelector(query, scope)

    override fun xpath(xpath: String, fetch: ContentFetchType?, waitParam: Any?) =
        SingleWebElementDelegate(
            ScopedXpathSelector(xpath, scope),
            fetch ?: browser.config.elementsFetchType,
            browser,
            waitParam ?: false
        )

    override fun xpath(
        xpath: String,
        scope: WebElement,
        fetch: ContentFetchType?,
        waitParam: Any?
    ) = SingleWebElementDelegate(
        ScopedXpathSelector(xpath, scope),
        fetch ?: browser.config.elementsFetchType,
        browser,
        waitParam ?: false
    )

    override fun xpath(
        xpath: String,
        scopeDelegate: SingleWebElementDelegate,
        fetch: ContentFetchType?,
        waitParam: Any?
    ): SingleWebElementDelegate {
        val scope by scopeDelegate
        return SingleWebElementDelegate(
            ScopedXpathSelector(xpath, scope),
            fetch ?: browser.config.elementsFetchType,
            browser,
            waitParam ?: false
        )
    }

    override fun xpathList(xpath: String, fetch: ContentFetchType?, waitParam: Any?) =
        WebElementsListDelegate(
            ScopedXpathSelector(xpath, scope),
            fetch ?: browser.config.elementsFetchType,
            browser,
            waitParam ?: false
        )

    override fun xpathList(
        xpath: String,
        scope: WebElement,
        fetch: ContentFetchType?,
        waitParam: Any?
    ) = WebElementsListDelegate(
        ScopedXpathSelector(xpath, scope),
        fetch ?: browser.config.elementsFetchType,
        browser,
        waitParam ?: false
    )

    override fun xpathList(
        xpath: String,
        scopeDelegate: SingleWebElementDelegate,
        fetch: ContentFetchType?,
        waitParam: Any?
    ): WebElementsListDelegate {
        val scope by scopeDelegate
        return WebElementsListDelegate(
            ScopedXpathSelector(xpath, scope),
            fetch ?: browser.config.elementsFetchType,
            browser,
            waitParam ?: false
        )
    }

    override fun <T : Module> module(factory: (Browser) -> T): ModuleDelegate<T> = ModuleDelegate(factory, browser)
    override fun <T : ScopedModule> scopedModule(
        factory: (Browser, WebElement) -> T,
        scope: Selector,
        fetch: ContentFetchType?,
        waitParam: Any?
    ) = ScopedModuleDelegate(
        scope,
        factory,
        fetch ?: browser.config.modulesFetchType,
        browser,
        waitParam ?: false
    )
}