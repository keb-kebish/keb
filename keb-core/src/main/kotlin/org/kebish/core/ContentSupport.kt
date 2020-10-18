package org.kebish.core

import org.kebish.core.browser.Browser
import org.kebish.core.content.*
import org.openqa.selenium.By
import org.openqa.selenium.WebElement

public interface ContentSupport {
    public val browser: Browser
    public fun getDefaultScope(): WebElement? = null

    public fun <T : Any?> content(
        required: Boolean = true,
        cache: Boolean = false,
        wait: Boolean = false,
        initializer: () -> T
    ): Content<T> = contentInternal(required, cache, wait, initializer)

    public fun <T : Any?> content(
        required: Boolean = true,
        cache: Boolean = false,
        wait: String,
        initializer: () -> T
    ): Content<T> = contentInternal(required, cache, wait, initializer)

    public fun <T : Any?> content(
        required: Boolean = true,
        cache: Boolean = false,
        wait: Number,
        initializer: () -> T
    ): Content<T> = contentInternal(required, cache, wait, initializer)


    public fun <T : Any?> content(
        required: Boolean = true,
        cache: Boolean = false,
        waitTimeout: Number,
        waitRetryInterval: Number,
        initializer: () -> T
    ): Content<T> = content(required, cache, WaitPreset(waitTimeout, waitRetryInterval), initializer)

    public fun <T : Any?> content(
        required: Boolean = true,
        cache: Boolean = false,
        wait: WaitPreset,
        initializer: () -> T
    ): Content<T> = contentInternal(required, cache, wait, initializer)

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

    public fun css(selector: String, scope: WebElement? = getDefaultScope()): WebElement =
        cssSelector(browser, selector, scope).getWebElement()

    public fun cssList(selector: String, scope: WebElement? = getDefaultScope()): List<WebElement> =
        cssSelector(browser, selector, scope).getWebElements()

    public fun html(selector: String, scope: WebElement? = getDefaultScope()): WebElement =
        htmlSelector(browser, selector, scope).getWebElement()

    public fun htmlList(selector: String, scope: WebElement? = getDefaultScope()): List<WebElement> =
        htmlSelector(browser, selector, scope).getWebElements()

    public fun xpath(selector: String, scope: WebElement? = getDefaultScope()): WebElement =
        xpathSelector(browser, selector, scope).getWebElement()

    public fun xpathList(selector: String, scope: WebElement? = getDefaultScope()): List<WebElement> =
        xpathSelector(browser, selector, scope).getWebElements()

    public fun by(selector: By, scope: WebElement? = getDefaultScope()): WebElement =
        bySelector(browser, selector, scope).getWebElement()

    public fun byList(selector: By, scope: WebElement? = getDefaultScope()): List<WebElement> =
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