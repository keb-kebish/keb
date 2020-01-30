package org.kebish.core

import org.openqa.selenium.By
import org.openqa.selenium.WebElement

interface ContentSupport {
    val browser: Browser
    fun getDefaultScope(): WebElement? = null

    fun <T> content(
        required: Boolean = true,
        cache: Boolean = false,
        wait: Boolean = false,
        initializer: () -> T
    ) = Content(
        CachingContentInitializer(
            cache,
            waitingInitializerFactory(
                wait,
                RequiredCheckingContentInitializer(
                    required,
                    ContentProvidingInitializer(initializer)
                )

            )
        )
    )

    fun <T> content(
        required: Boolean = true,
        cache: Boolean = false,
        wait: Number,
        initializer: () -> T
    ) = Content(
        CachingContentInitializer(
            cache,
            waitingInitializerFactory(
                wait,
                RequiredCheckingContentInitializer(
                    required,
                    ContentProvidingInitializer(initializer)
                )

            )
        )
    )

    fun <T> content(
        required: Boolean = true,
        cache: Boolean = false,
        wait: Pair<Number, Number>,
        initializer: () -> T
    ) = Content(
        CachingContentInitializer(
            cache,
            waitingInitializerFactory(
                wait,
                RequiredCheckingContentInitializer(
                    required,
                    ContentProvidingInitializer(initializer)
                )

            )
        )
    )

    fun <T> content(
        required: Boolean = true,
        cache: Boolean = false,
        wait: String,
        initializer: () -> T
    ) = Content(
        CachingContentInitializer(
            cache,
            waitingInitializerFactory(
                wait,
                RequiredCheckingContentInitializer(
                    required,
                    ContentProvidingInitializer(initializer)
                )
            )
        )
    )

    private fun <T> waitingInitializerFactory(wait: Any, decorated: ContentInitializer<T>): ContentInitializer<T> {
        return when (wait) {
            is Boolean -> WaitingContentInitializer(wait, browser, decorated)
            is Number -> WaitingByTimeoutContentInitializer(wait, browser, decorated)
            is Pair<*, *> -> WaitingByTimeoutAndRetryIntervalContentInitializer(
                wait.first as Number,
                wait.second as Number,
                browser,
                decorated
            )
            is String -> WaitingByPresetNameContentInitializer(wait, browser, decorated)
            else -> throw IllegalArgumentException("Unexpected 'wait' parameter type '${wait.javaClass.name}'.")
        }
    }
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