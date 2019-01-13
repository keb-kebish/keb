package com.horcacorp.testing.keb.core

import org.openqa.selenium.WebElement

abstract class ScopedModule(private val browser: Browser, private val scope: WebElement) : ContentSupport,
    NavigationSupport by browser, WaitSupport by browser {

    override fun css(selector: String, fetch: ContentFetchType?, waitParam: Any?): SingleWebElementDelegate {
        return SingleWebElementDelegate(
            ScopedCssSelector(selector, scope),
            fetch ?: browser.config.elementsFetchType,
            browser,
            waitParam ?: browser.config.ELEMENT_WAIT_PRESET_NAME
        )
    }

    override fun cssList(selector: String, fetch: ContentFetchType?, waitParam: Any?): WebElementsListDelegate {
        return WebElementsListDelegate(
            ScopedCssSelector(selector, scope),
            fetch ?: browser.config.elementsFetchType,
            browser,
            waitParam ?: browser.config.ELEMENT_WAIT_PRESET_NAME
        )
    }

    override fun html(tag: String, fetch: ContentFetchType?, waitParam: Any?): SingleWebElementDelegate {
        return SingleWebElementDelegate(
            ScopedHtmlSelector(tag, scope),
            fetch ?: browser.config.elementsFetchType,
            browser,
            waitParam ?: browser.config.ELEMENT_WAIT_PRESET_NAME
        )
    }

    override fun htmlList(tag: String, fetch: ContentFetchType?, waitParam: Any?): WebElementsListDelegate {
        return WebElementsListDelegate(
            ScopedHtmlSelector(tag, scope),
            fetch ?: browser.config.elementsFetchType,
            browser,
            waitParam ?: browser.config.ELEMENT_WAIT_PRESET_NAME
        )
    }

    override fun xpath(xpath: String, fetch: ContentFetchType?, waitParam: Any?): SingleWebElementDelegate {
        return SingleWebElementDelegate(
            ScopedXpathSelector(xpath, scope),
            fetch ?: browser.config.elementsFetchType,
            browser,
            waitParam ?: browser.config.ELEMENT_WAIT_PRESET_NAME
        )
    }

    override fun xpathList(xpath: String, fetch: ContentFetchType?, waitParam: Any?): WebElementsListDelegate {
        return WebElementsListDelegate(
            ScopedXpathSelector(xpath, scope),
            fetch ?: browser.config.elementsFetchType,
            browser,
            waitParam ?: browser.config.ELEMENT_WAIT_PRESET_NAME
        )
    }

    override fun <T : Module> module(factory: (Browser) -> T): ModuleDelegate<T> {
        return ModuleDelegate(factory, browser)
    }

    override fun <T : ScopedModule> scopedModule(
        factory: (Browser, WebElement) -> T,
        scope: SingleWebElementDelegate,
        fetch: ContentFetchType?,
        waitParam: Any?
    ): ScopedModuleDelegate<T> {
        return ScopedModuleDelegate(
            scope,
            factory,
            fetch ?: browser.config.modulesFetchType,
            browser,
            waitParam ?: browser.config.MODULE_SCOPE_WAIT_PRESET_NAME
        )
    }

}