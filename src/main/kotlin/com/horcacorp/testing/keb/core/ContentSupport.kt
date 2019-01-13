package com.horcacorp.testing.keb.core

import org.openqa.selenium.WebElement

enum class ContentFetchType {
    ON_EVERY_ACCESS, ON_FIRST_ACCESS
}

interface ContentSupport {

    fun css(selector: String, fetch: ContentFetchType? = null, waitParam: Any? = null): SingleWebElementDelegate

    fun cssList(selector: String, fetch: ContentFetchType? = null, waitParam: Any? = null): WebElementsListDelegate

    fun html(tag: String, fetch: ContentFetchType? = null, waitParam: Any? = null): SingleWebElementDelegate

    fun htmlList(tag: String, fetch: ContentFetchType? = null, waitParam: Any? = null): WebElementsListDelegate

    fun xpath(xpath: String, fetch: ContentFetchType? = null, waitParam: Any? = null): SingleWebElementDelegate

    fun xpathList(xpath: String, fetch: ContentFetchType? = null, waitParam: Any? = null): WebElementsListDelegate

    fun <T : Module> module(factory: (Browser) -> T): ModuleDelegate<T>

    fun <T : ScopedModule> scopedModule(
        factory: (Browser, WebElement) -> T,
        scope: SingleWebElementDelegate,
        fetch: ContentFetchType? = null,
        waitParam: Any? = null
    ): ScopedModuleDelegate<T>

}