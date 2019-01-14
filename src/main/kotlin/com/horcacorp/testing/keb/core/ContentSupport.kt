package com.horcacorp.testing.keb.core

import org.openqa.selenium.WebElement

enum class ContentFetchType {
    ON_EVERY_ACCESS, ON_FIRST_ACCESS
}

interface ContentSupport {

    fun cssSelector(query: String): Selector
    fun css(selector: String, fetch: ContentFetchType? = null, waitParam: Any? = null): SingleWebElementDelegate
    fun css(
        selector: String,
        scope: WebElement,
        fetch: ContentFetchType? = null,
        waitParam: Any? = null
    ): SingleWebElementDelegate
    fun css(
        selector: String,
        scopeDelegate: SingleWebElementDelegate,
        fetch: ContentFetchType? = null,
        waitParam: Any? = null
    ): SingleWebElementDelegate

    fun cssList(selector: String, fetch: ContentFetchType? = null, waitParam: Any? = null): WebElementsListDelegate
    fun cssList(
        selector: String,
        scope: WebElement,
        fetch: ContentFetchType? = null,
        waitParam: Any? = null
    ): WebElementsListDelegate
    fun cssList(
        selector: String,
        scopeDelegate: SingleWebElementDelegate,
        fetch: ContentFetchType? = null,
        waitParam: Any? = null
    ): WebElementsListDelegate

    fun htmlSelector(query: String): Selector
    fun html(tag: String, fetch: ContentFetchType? = null, waitParam: Any? = null): SingleWebElementDelegate
    fun html(
        tag: String,
        scope: WebElement,
        fetch: ContentFetchType? = null,
        waitParam: Any? = null
    ): SingleWebElementDelegate
    fun html(
        tag: String,
        scopeDelegate: SingleWebElementDelegate,
        fetch: ContentFetchType? = null,
        waitParam: Any? = null
    ): SingleWebElementDelegate

    fun htmlList(tag: String, fetch: ContentFetchType? = null, waitParam: Any? = null): WebElementsListDelegate
    fun htmlList(
        tag: String,
        scope: WebElement,
        fetch: ContentFetchType? = null,
        waitParam: Any? = null
    ): WebElementsListDelegate
    fun htmlList(
        tag: String,
        scopeDelegate: SingleWebElementDelegate,
        fetch: ContentFetchType? = null,
        waitParam: Any? = null
    ): WebElementsListDelegate

    fun xpathSelector(query: String): Selector
    fun xpath(xpath: String, fetch: ContentFetchType? = null, waitParam: Any? = null): SingleWebElementDelegate
    fun xpath(
        xpath: String,
        scope: WebElement,
        fetch: ContentFetchType? = null,
        waitParam: Any? = null
    ): SingleWebElementDelegate
    fun xpath(
        xpath: String,
        scopeDelegate: SingleWebElementDelegate,
        fetch: ContentFetchType? = null,
        waitParam: Any? = null
    ): SingleWebElementDelegate

    fun xpathList(xpath: String, fetch: ContentFetchType? = null, waitParam: Any? = null): WebElementsListDelegate
    fun xpathList(
        xpath: String,
        scope: WebElement,
        fetch: ContentFetchType? = null,
        waitParam: Any? = null
    ): WebElementsListDelegate
    fun xpathList(
        xpath: String,
        scopeDelegate: SingleWebElementDelegate,
        fetch: ContentFetchType? = null,
        waitParam: Any? = null
    ): WebElementsListDelegate

    fun <T : Module> module(factory: (Browser) -> T): ModuleDelegate<T>
    fun<T :ScopedModule> scopedModule(
        factory: (Browser, WebElement) -> T,
        scope: Selector,
        fetch: ContentFetchType? = null,
        waitParam: Any? = null
    ) : ScopedModuleDelegate<T>

}