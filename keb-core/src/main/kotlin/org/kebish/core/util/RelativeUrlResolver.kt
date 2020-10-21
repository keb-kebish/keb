package org.kebish.core.util

import org.kebish.core.browser.Browser
import java.net.URI
import kotlin.reflect.KProperty

internal class RelativeUrlResolver(val browser: Browser) {

    operator fun getValue(browser: Browser, property: KProperty<*>): String {
        return browser.driver.currentUrl
    }

    operator fun setValue(browser: Browser, property: KProperty<*>, newUrl: String) {
        val newUri = calculateAbsoluteUri(newUrl)
        browser.driver.get(newUri.toString())
    }

    private fun calculateAbsoluteUri(path: String): URI {
        if (path.isEmpty()) {
            return URI(getBaseUrlRequired())
        }

        var absolute = URI(path)
        if (!absolute.isAbsolute) {
            var baseUrl = getBaseUrlRequired()
            if (!baseUrl.endsWith('/')) {
                baseUrl += "/"
            }
            absolute = URI(baseUrl).resolve(absolute)
        }
        return absolute
    }

    private fun getBaseUrlRequired(): String {
        val baseUrl = browser.baseUrl
        if (baseUrl.isBlank()) {
            throw NoBaseUrlDefinedException()
        }
        return baseUrl
    }

}

private class NoBaseUrlDefinedException : RuntimeException(
    "There is no base URL configured and it was requested. " +
            "(quick solution: you can set 'baseUrl' in your KebConfig, or use absolute url)"
)