package com.horcacorp.testing.keb.core

import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.edge.EdgeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.opera.OperaDriver
import java.net.URI

class Browser(val config: Configuration) : ContentSupport, NavigationSupport, WaitSupport {

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

    private val driver by lazy { resolveWebDriver() }

    override fun cssSelector(query: String) = CssSelector(query, driver)
    override fun css(selector: String, fetch: ContentFetchType?, waitParam: Any?) =
        SingleWebElementDelegate(
            CssSelector(selector, driver),
            fetch ?: config.elementsFetchType,
            this,
            waitParam ?: false
        )

    override fun css(
        selector: String,
        scope: WebElement,
        fetch: ContentFetchType?,
        waitParam: Any?
    ) = SingleWebElementDelegate(
        ScopedCssSelector(selector, scope),
        fetch ?: config.elementsFetchType,
        this,
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
            fetch ?: config.elementsFetchType,
            this,
            waitParam ?: false
        )
    }

    override fun cssList(selector: String, fetch: ContentFetchType?, waitParam: Any?) =
        WebElementsListDelegate(
            CssSelector(selector, driver),
            fetch ?: config.elementsFetchType,
            this,
            waitParam ?: false
        )

    override fun cssList(
        selector: String,
        scope: WebElement,
        fetch: ContentFetchType?,
        waitParam: Any?
    ) = WebElementsListDelegate(
        ScopedCssSelector(selector, scope),
        fetch ?: config.elementsFetchType,
        this,
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
            fetch ?: config.elementsFetchType,
            this,
            waitParam ?: false
        )
    }

    override fun htmlSelector(query: String) = HtmlSelector(query, driver)

    override fun html(tag: String, fetch: ContentFetchType?, waitParam: Any?) =
        SingleWebElementDelegate(
            HtmlSelector(tag, driver),
            fetch ?: config.elementsFetchType,
            this,
            waitParam ?: false
        )

    override fun html(
        tag: String,
        scope: WebElement,
        fetch: ContentFetchType?,
        waitParam: Any?
    ) = SingleWebElementDelegate(
        ScopedHtmlSelector(tag, scope),
        fetch ?: config.elementsFetchType,
        this,
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
            fetch ?: config.elementsFetchType,
            this,
            waitParam ?: false
        )
    }

    override fun htmlList(tag: String, fetch: ContentFetchType?, waitParam: Any?) =
        WebElementsListDelegate(
            HtmlSelector(tag, driver),
            fetch ?: config.elementsFetchType,
            this,
            waitParam ?: false
        )

    override fun htmlList(
        tag: String,
        scope: WebElement,
        fetch: ContentFetchType?,
        waitParam: Any?
    ) = WebElementsListDelegate(
        ScopedHtmlSelector(tag, scope),
        fetch ?: config.elementsFetchType,
        this,
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
            fetch ?: config.elementsFetchType,
            this,
            waitParam ?: false
        )
    }

    override fun xpathSelector(query: String) = XPathSelector(query, driver)

    override fun xpath(xpath: String, fetch: ContentFetchType?, waitParam: Any?) =
        SingleWebElementDelegate(
            XPathSelector(xpath, driver),
            fetch ?: config.elementsFetchType,
            this,
            waitParam ?: false
        )

    override fun xpath(
        xpath: String,
        scope: WebElement,
        fetch: ContentFetchType?,
        waitParam: Any?
    ) = SingleWebElementDelegate(
        ScopedXpathSelector(xpath, scope),
        fetch ?: config.elementsFetchType,
        this,
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
            fetch ?: config.elementsFetchType,
            this,
            waitParam ?: false
        )
    }

    override fun xpathList(xpath: String, fetch: ContentFetchType?, waitParam: Any?) =
        WebElementsListDelegate(
            XPathSelector(xpath, driver),
            fetch ?: config.elementsFetchType,
            this,
            waitParam ?: false
        )

    override fun xpathList(
        xpath: String,
        scope: WebElement,
        fetch: ContentFetchType?,
        waitParam: Any?
    ) = WebElementsListDelegate(
        ScopedXpathSelector(xpath, scope),
        fetch ?: config.elementsFetchType,
        this,
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
            fetch ?: config.elementsFetchType,
            this,
            waitParam ?: false
        )
    }

    override fun <T : Module> module(factory: (Browser) -> T) = ModuleDelegate(factory, this)
    override fun <T : ScopedModule> scopedModule(
        factory: (Browser, WebElement) -> T,
        scope: Selector,
        fetch: ContentFetchType?,
        waitParam: Any?
    ) = ScopedModuleDelegate(
        scope,
        factory,
        fetch ?: config.modulesFetchType,
        this,
        waitParam ?: false
    )

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

    fun quit() {
        driver.quit()
    }

    private fun resolveWebDriver() = when (config.browserType) {
        BrowserType.CHROME -> {
            WebDriverManager.chromedriver().setup()
            ChromeDriver()
        }
        BrowserType.FIREFOX -> {
            WebDriverManager.firefoxdriver().setup()
            FirefoxDriver()
        }
        BrowserType.OPERA -> {
            WebDriverManager.operadriver().setup()
            OperaDriver()
        }
        BrowserType.EDGE -> {
            WebDriverManager.edgedriver().setup()
            EdgeDriver()
        }
        BrowserType.IE -> {
            WebDriverManager.iedriver().setup()
            InternetExplorerDriver()
        }
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

enum class BrowserType {
    CHROME, FIREFOX, OPERA, EDGE, IE
}