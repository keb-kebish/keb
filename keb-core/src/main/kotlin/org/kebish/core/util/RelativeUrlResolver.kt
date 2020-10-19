package org.kebish.core.util

import org.kebish.core.browser.Browser
import kotlin.reflect.KProperty

internal class RelativeUrlResolver(val browser: Browser) {

    operator fun getValue(browser: Browser, property: KProperty<*>): String {
        return browser.driver.currentUrl
    }

    operator fun setValue(browser: Browser, property: KProperty<*>, newUrl: String) {
        //        set(url) = driver.get(url) //TODO work with baseUrl - e.g. baseUrl=kebish.org, then  set("hello") will open "kebish.org/hello"
        browser.driver.get(newUrl)
    }


}