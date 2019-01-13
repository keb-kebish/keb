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

    override fun css(selector: String, fetch: ContentFetchType?, waitParam: Any?): SingleWebElementDelegate {
        return SingleWebElementDelegate(
            CssSelector(selector, driver),
            fetch ?: config.elementsFetchType,
            this,
            waitParam ?: config.ELEMENT_WAIT_PRESET_NAME
        )
    }

    override fun cssList(selector: String, fetch: ContentFetchType?, waitParam: Any?): WebElementsListDelegate {
        return WebElementsListDelegate(
            CssSelector(selector, driver),
            fetch ?: config.elementsFetchType,
            this,
            waitParam ?: config.ELEMENT_WAIT_PRESET_NAME
        )
    }

    override fun html(tag: String, fetch: ContentFetchType?, waitParam: Any?): SingleWebElementDelegate {
        return SingleWebElementDelegate(
            HtmlSelector(tag, driver),
            fetch ?: config.elementsFetchType,
            this,
            waitParam ?: config.ELEMENT_WAIT_PRESET_NAME
        )
    }

    override fun htmlList(tag: String, fetch: ContentFetchType?, waitParam: Any?): WebElementsListDelegate {
        return WebElementsListDelegate(
            HtmlSelector(tag, driver),
            fetch ?: config.elementsFetchType,
            this,
            waitParam ?: config.ELEMENT_WAIT_PRESET_NAME
        )
    }

    override fun xpath(xpath: String, fetch: ContentFetchType?, waitParam: Any?): SingleWebElementDelegate {
        return SingleWebElementDelegate(
            XPathSelector(xpath, driver),
            fetch ?: config.elementsFetchType,
            this,
            waitParam ?: config.ELEMENT_WAIT_PRESET_NAME
        )
    }

    override fun xpathList(xpath: String, fetch: ContentFetchType?, waitParam: Any?): WebElementsListDelegate {
        return WebElementsListDelegate(
            XPathSelector(xpath, driver),
            fetch ?: config.elementsFetchType,
            this,
            waitParam ?: config.ELEMENT_WAIT_PRESET_NAME
        )
    }

    override fun <T : Module> module(factory: (Browser) -> T) = ModuleDelegate(factory, this)

    override fun <T : ScopedModule> scopedModule(
        factory: (Browser, WebElement) -> T,
        scope: SingleWebElementDelegate,
        fetch: ContentFetchType?,
        waitParam: Any?
    ) = ScopedModuleDelegate(
        scope,
        factory,
        fetch ?: config.modulesFetchType,
        this,
        waitParam ?: config.MODULE_SCOPE_WAIT_PRESET_NAME
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
            is Boolean -> if (waitParam) waitFor("default", desc, f) else f()
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

    override fun <T : Page> to(factory: (Browser) -> T): T = factory(this).apply {
        driver.get(resolveUrl(url()))
        verifyAt()
    }

    override fun <T : Page> at(factory: (Browser) -> T): T = factory(this).apply {
        verifyAt()
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