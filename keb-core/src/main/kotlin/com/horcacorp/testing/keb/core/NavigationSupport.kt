package com.horcacorp.testing.keb.core

import java.net.URI

interface NavigationSupport {

    val browser: Browser


    fun <T : Page> to(pageFactory: () -> T, waitPreset: String? = null): T {
        val page = pageFactory()
        return to(page, waitPreset, { page })
    }

    fun <T : Page, R : Any> to(pageFactory: () -> T, waitPreset: String? = null, body: T.() -> R): R =
        to(pageFactory(), waitPreset, body)


    fun <T : Page> to(page: T, waitPreset: String? = null): T =
        to(page, waitPreset, { page })

    fun <T : Page, R : Any> to(page: T, waitPreset: String? = null, body: T.() -> R): R {
        browser.driver.get(resolveUrl(page.url()))
        return at(page, waitPreset, body)
    }

    private fun resolveUrl(urlSuffix: String): String {
        val urlPrefix = browser.baseUrl
        val url = if (urlPrefix.isEmpty()) urlSuffix else "$urlPrefix/$urlSuffix"
        return URI(url).normalize().toString()
    }


    fun <T : Page> at(pageFactory: () -> T, waitPreset: String? = null): T {
        val page = pageFactory()
        return at(page, waitPreset, { page })
    }

    fun <T : Page, R : Any> at(pageFactory: () -> T, waitPreset: String? = null, body: T.() -> R): R =
        at(pageFactory(), waitPreset, body)


    fun <T : Page> at(page: T, waitPreset: String? = null): T =
        at(page, waitPreset) { page }


    fun <T : Page, R : Any> at(page: T, waitPreset: String? = null, body: T.() -> R): R {
        page.browser = browser
        page.verifyAt(waitPreset)
        return body.invoke(page)
    }


    fun <T : Page, R : Any> T.via(body: T.() -> R): R {
        return body(this)
    }


}