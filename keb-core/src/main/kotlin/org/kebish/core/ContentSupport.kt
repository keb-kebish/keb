package org.kebish.core

import org.kebish.core.browser.Browser
import org.kebish.core.content.*
import org.openqa.selenium.By
import org.openqa.selenium.WebElement

interface ContentSupport {
    val browser: Browser
    fun getDefaultScope(): WebElement? = null

    fun <T : Any?> content(
        required: Boolean = true,
        cache: Boolean = false,
        wait: Boolean = false,
        initializer: () -> T
    ) = contentInternal(required, cache, wait, initializer)

    fun <T : Any?> content(
        required: Boolean = true,
        cache: Boolean = false,
        wait: String,
        initializer: () -> T
    ) = contentInternal(required, cache, wait, initializer)

    fun <T : Any?> content(
        required: Boolean = true,
        cache: Boolean = false,
        wait: Number,
        initializer: () -> T
    ) = contentInternal(required, cache, wait, initializer)


    fun <T : Any?> content(
        required: Boolean = true,
        cache: Boolean = false,
        waitTimeout: Number,
        waitRetryInterval: Number,
        initializer: () -> T
    ) = content(required, cache, WaitPreset(waitTimeout,waitRetryInterval), initializer)

    fun <T : Any?> content(
        required: Boolean = true,
        cache: Boolean = false,
        wait: WaitPreset,
        initializer: () -> T
    ) = contentInternal(required, cache, wait, initializer)

    private fun <T> contentInternal(required: Boolean, cache: Boolean, wait: Any, initializer: () -> T) = Content(
        CachingContentInitializer(
            cache,
            WaitingContentInitializer(
                wait,
                RequiredCheckingContentInitializer(
                    required,
                    ContentProvidingInitializer(initializer)
                )

            )
        )
    )

    fun css(selector: String, scope: WebElement? = getDefaultScope()) =
        cssSelector(browser, selector, scope).getWebElement()

    fun cssList(selector: String, scope: WebElement? = getDefaultScope()) =
        cssSelector(browser, selector, scope).getWebElements()

    fun html(selector: String, scope: WebElement? = getDefaultScope()) =
        htmlSelector(browser, selector, scope).getWebElement()

    fun htmlList(selector: String, scope: WebElement? = getDefaultScope()) =
        htmlSelector(browser, selector, scope).getWebElements()

    fun xpath(selector: String, scope: WebElement? = getDefaultScope()) =
        xpathSelector(browser, selector, scope).getWebElement()

    fun xpathList(selector: String, scope: WebElement? = getDefaultScope()) =
        xpathSelector(browser, selector, scope).getWebElements()

    fun by(selector: By, scope: WebElement? = getDefaultScope()) = bySelector(browser, selector, scope).getWebElement()

    fun byList(selector: By, scope: WebElement? = getDefaultScope()) =
        bySelector(browser, selector, scope).getWebElements()

    private fun cssSelector(browser: Browser, selector: String, scope: WebElement?) =
        CssSelector(selector, scope ?: browser.driver)

    private fun htmlSelector(browser: Browser, tag: String, scope: WebElement?) =
        HtmlSelector(tag, scope ?: browser.driver)

    private fun xpathSelector(browser: Browser, xpath: String, scope: WebElement?) =
        XPathSelector(xpath, scope ?: browser.driver)

    private fun bySelector(browser: Browser, by: By, scope: WebElement?) =
        BySelector(by, scope ?: browser.driver)
}