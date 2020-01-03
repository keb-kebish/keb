package com.horcacorp.testing.keb.core

import java.net.URI

interface NavigationSupport {

    val browser: Browser

    fun <T : Page> to(pageFactory: () -> T, waitPreset: String? = null, body: T.() -> Unit = {}) =
        to(pageFactory(), waitPreset, body)


    fun <T : Page> to(page: T, waitPreset: String? = null, body: T.() -> Unit = {}): T {
        browser.driver.get(resolveUrl(page.url()))
        return at(page, waitPreset, body)
    }

    fun <T : Page> at(pageFactory: () -> T, waitPreset: String? = null, body: T.() -> Unit = {}) =
        at(pageFactory(), waitPreset, body)

    fun <T : Page> at(page: T, waitPreset: String? = null, body: T.() -> Unit = {}): T {
        page.browser = browser
        page.verifyAt(waitPreset)
        body.invoke(page)
        return page
    }

    private fun resolveUrl(urlSuffix: String): String {
        val urlPrefix = browser.baseUrl
        val url = if (urlPrefix.isEmpty()) urlSuffix else "$urlPrefix/$urlSuffix"
        return URI(url).normalize().toString()
    }

}