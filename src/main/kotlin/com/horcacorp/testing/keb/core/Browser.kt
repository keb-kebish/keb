package com.horcacorp.testing.keb.core

import org.openqa.selenium.WebElement
import java.net.URI

class Browser(val config: Configuration) : ContentSupport, NavigationSupport, WaitSupport, ModuleSupport {

    companion object {
        fun drive(block: Browser.() -> Unit) {
            val browser = Browser(Configuration())
            try {
                block(browser)
            } finally {
                browser.quit()
            }
        }
    }

    val driver = config.driver ?: throw IllegalStateException("Browser is not initialized.")

    override fun css(
        selector: String,
        scope: WebElement?,
        fetch: ContentFetchType?,
        waitParam: Any?
    ): WebElement =
        WebElementDelegate(
            scope?.let { ScopedCssSelector(selector, it) } ?: CssSelector(selector, driver),
            fetch ?: config.elementsFetchType,
            this,
            waitParam ?: false
        )

    override fun cssList(
        selector: String,
        scope: WebElement?,
        fetch: ContentFetchType?,
        waitParam: Any?
    ): List<WebElement> =
        WebElementsListDelegate(
            scope?.let { ScopedCssSelector(selector, it) } ?: CssSelector(selector, driver),
            fetch ?: config.elementsFetchType,
            this,
            waitParam ?: false
        )

    override fun html(tag: String, scope: WebElement?, fetch: ContentFetchType?, waitParam: Any?): WebElement =
        WebElementDelegate(
            scope?.let { ScopedHtmlSelector(tag, it) } ?: HtmlSelector(tag, driver),
            fetch ?: config.elementsFetchType,
            this,
            waitParam ?: false
        )

    override fun htmlList(
        tag: String,
        scope: WebElement?,
        fetch: ContentFetchType?,
        waitParam: Any?
    ): List<WebElement> =
        WebElementsListDelegate(
            scope?.let { ScopedHtmlSelector(tag, it) } ?: HtmlSelector(tag, driver),
            fetch ?: config.elementsFetchType,
            this,
            waitParam ?: false
        )

    override fun xpath(
        xpath: String,
        scope: WebElement?,
        fetch: ContentFetchType?,
        waitParam: Any?
    ): WebElement =
        WebElementDelegate(
            scope?.let { ScopedXpathSelector(xpath, it) } ?: XPathSelector(xpath, driver),
            fetch ?: config.elementsFetchType,
            this,
            waitParam ?: false
        )

    override fun xpathList(
        xpath: String,
        scope: WebElement?,
        fetch: ContentFetchType?,
        waitParam: Any?
    ): List<WebElement> =
        WebElementsListDelegate(
            scope?.let { ScopedXpathSelector(xpath, it) } ?: XPathSelector(xpath, driver),
            fetch ?: config.elementsFetchType,
            this,
            waitParam ?: false
        )

    override fun <T : Module> module(factory: (Browser) -> T): T = factory(this)

    override fun <T : ScopedModule> scopedModule(
        factory: (Browser, WebElement) -> T,
        scope: WebElement
    ): T =
        factory(this, scope)

    override fun <T> waitFor(waitParam: Any, desc: String?, f: () -> T): T {
        return when (waitParam) {
            is String -> waitFor(waitParam.toUpperCase(), desc, f)
            is Pair<*, *> -> {
                if (waitParam.first is Number && waitParam.second is Number) {
                    waitFor((waitParam.first as Number).toLong(), (waitParam.second as Number).toLong(), desc, f)
                } else {
                    throw IllegalArgumentException("Expecting pair of numbers signaling timeout and retry interval in millis.")
                }
            }
            is Boolean -> if (waitParam) waitFor(config.DEFAULT_WAIT_PRESET_NAME, desc, f) else f()
            else -> throw IllegalArgumentException("${waitParam::javaClass} is not applicable as wait parameter.")
        }
    }

    override fun <T> waitFor(presetName: String, desc: String?, f: () -> T): T {
        val preset = config.waitPresets[presetName.toUpperCase()] ?: throw WaitPresetNotFoundException(presetName)
        return waitFor(preset.timeoutMillis, preset.retryIntervalMillis, desc, f)
    }

    override fun <T> waitFor(timeoutMillis: Long, retryIntervalMillis: Long, desc: String?, f: () -> T): T {
        val timeoutAt = System.currentTimeMillis() + timeoutMillis
        var passed = false
        var value: T? = null
        var thrown: Throwable? = null

        try {
            value = f()
            passed = resolveTruthiness(value)
        } catch (t: Throwable) {
            thrown = t
        }

        var timedOut = System.currentTimeMillis() > timeoutAt
        while (!passed && !timedOut) {
            Thread.sleep(retryIntervalMillis)
            try {
                value = f()
                passed = resolveTruthiness(value)
                thrown = null
            } catch (t: Throwable) {
                thrown = t
            } finally {
                timedOut = System.currentTimeMillis() > timeoutAt
            }
        }

        return if (passed) {
            value!!
        } else {
            val err = if (desc != null) {
                "Waiting for '$desc' has timed out after $timeoutMillis milliseconds."
            } else {
                "Waiting has timed out after $timeoutMillis milliseconds."
            }
            throw WaitTimeoutException(err, thrown)
        }
    }

    override fun <T : Page> to(factory: (Browser) -> T, waitParam: Any?): T = factory(this).apply {
        driver.get(resolveUrl(url()))
        verifyAt(waitParam)
    }

    override fun <T : Page> at(factory: (Browser) -> T, waitParam: Any?): T = factory(this).apply {
        verifyAt(waitParam)
    }

    override fun <T> withNewTab(action: () -> T): T {
        val currentTabIndex = driver.windowHandles.indexOf(driver.windowHandle)
        val nextTabHandle = driver.windowHandles.elementAt(currentTabIndex + 1)
        driver.switchTo().window(nextTabHandle)
        return action()
    }

    override fun <T> withClosedTab(action: () -> T): T {
        val currentTabIndex = driver.windowHandles.indexOf(driver.windowHandle)
        val previous = driver.windowHandles.elementAt(currentTabIndex - 1)
        driver.switchTo().window(previous)
        return action()
    }

    fun quit() {
        driver.quit()
    }

    private fun resolveUrl(urlSuffix: String): String {
        val urlPrefix = config.urlPrefix
        val url = if (urlPrefix.isEmpty()) urlSuffix else "$urlPrefix/$urlSuffix"
        return URI(url).normalize().toString()
    }

    private fun resolveTruthiness(value: Any?): Boolean {
        return when (value) {
            is Number -> value != 0
            is CharSequence -> value.length > 0
            is Boolean -> value
            is Collection<*> -> if (value.isEmpty()) false else value.all { resolveTruthiness(it) }
            is WebElement -> value.isDisplayed
            null -> false
            else -> true
        }
    }

}