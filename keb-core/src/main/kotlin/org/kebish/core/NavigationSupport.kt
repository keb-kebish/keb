package org.kebish.core

import org.kebish.core.browser.Browser
import org.kebish.core.page.Page
import java.net.URI
import kotlin.reflect.KClass

interface NavigationSupport {

    val browser: Browser

    // TO
    // boolean wait
    fun <T : Page> to(pageFactory: () -> T, wait: Boolean = true) = pageFactory().let { to(it, wait, { it }) }
    fun <T : Page, R : Any> to(pageFactory: () -> T, wait: Boolean = true, body: T.() -> R) = to(pageFactory(), wait, body)
    fun <T : Page> to(page: T, wait: Boolean = true) = to(page, wait, { page })
    fun <T : Page, R : Any> to(page: T, wait: Boolean = true, body: T.() -> R): R = browser.driver.get(resolveUrl(page.url())).run { at(page, wait, body) }
    // string wait
    fun <T : Page> to(pageFactory: () -> T, wait: String) = pageFactory().let { to(it, wait, { it }) }
    fun <T : Page, R : Any> to(pageFactory: () -> T, wait: String, body: T.() -> R) = to(pageFactory(), wait, body)
    fun <T : Page> to(page: T, wait: String) = to(page, wait, { page })
    fun <T : Page, R : Any> to(page: T, wait: String, body: T.() -> R): R = browser.driver.get(resolveUrl(page.url())).run { at(page, wait, body) }
    // number wait
    fun <T : Page> to(pageFactory: () -> T, wait: Number) = pageFactory().let { to(it, wait, { it }) }
    fun <T : Page, R : Any> to(pageFactory: () -> T, wait: Number, body: T.() -> R) = to(pageFactory(), wait, body)
    fun <T : Page> to(page: T, wait: Number) = to(page, wait, { page })
    fun <T : Page, R : Any> to(page: T, wait: Number, body: T.() -> R): R = browser.driver.get(resolveUrl(page.url())).run { at(page, wait, body) }
    // timeout + retry interval wait
    fun <T : Page> to(pageFactory: () -> T, waitTimeout: Number, waitRetryInterval: Number) = pageFactory().let { to(it, waitTimeout, waitRetryInterval, { it }) }
    fun <T : Page, R : Any> to(pageFactory: () -> T, waitTimeout: Number, waitRetryInterval: Number, body: T.() -> R) = to(pageFactory(), waitTimeout, waitRetryInterval, body)
    fun <T : Page> to(page: T, waitTimeout: Number, waitRetryInterval: Number) = to(page, waitTimeout, waitRetryInterval, { page })
    fun <T : Page, R : Any> to(page: T, waitTimeout: Number, waitRetryInterval: Number, body: T.() -> R): R = browser.driver.get(resolveUrl(page.url())).run { at(page, waitTimeout, waitRetryInterval, body) }
    // wait preset wait
    fun <T : Page> to(pageFactory: () -> T, wait: WaitPreset) = pageFactory().let { to(it, wait, { it }) }
    fun <T : Page, R : Any> to(pageFactory: () -> T, wait: WaitPreset, body: T.() -> R) = to(pageFactory(), wait, body)
    fun <T : Page> to(page: T, wait: WaitPreset) = to(page, wait, { page })
    fun <T : Page, R : Any> to(page: T, wait: WaitPreset, body: T.() -> R): R = browser.driver.get(resolveUrl(page.url())).run { at(page, wait, body) }

    // AT
    // boolean wait
    fun <T : Page> at(pageFactory: () -> T, wait: Boolean = true) = pageFactory().let { at(it, wait, { it }) }
    fun <T : Page, R : Any> at(pageFactory: () -> T, wait: Boolean = true, body: T.() -> R) = at(pageFactory(), wait, body)
    fun <T : Page> at(page: T, wait: Boolean = true) = at(page, wait) { page }
    fun <T : Page, R : Any> at(page: T, wait: Boolean = true, body: T.() -> R) = atInternal(page, wait, body)
    // string wait
    fun <T : Page> at(pageFactory: () -> T, wait: String) = pageFactory().let { at(it, wait, { it }) }
    fun <T : Page, R : Any> at(pageFactory: () -> T, wait: String, body: T.() -> R) = at(pageFactory(), wait, body)
    fun <T : Page> at(page: T, wait: String) = at(page, wait) { page }
    fun <T : Page, R : Any> at(page: T, wait: String, body: T.() -> R) = atInternal(page, wait, body)
    // number wait
    fun <T : Page> at(pageFactory: () -> T, wait: Number) = pageFactory().let { at(it, wait, { it }) }
    fun <T : Page, R : Any> at(pageFactory: () -> T, wait: Number, body: T.() -> R) = at(pageFactory(), wait, body)
    fun <T : Page> at(page: T, wait: Number) = at(page, wait) { page }
    fun <T : Page, R : Any> at(page: T, wait: Number, body: T.() -> R) = atInternal(page, wait, body)
    // timeout + retry interval wait
    fun <T : Page> at(pageFactory: () -> T, waitTimeout: Number, waitRetryInterval: Number) = pageFactory().let { at(it, waitTimeout, waitRetryInterval, { it }) }
    fun <T : Page, R : Any> at(pageFactory: () -> T, waitTimeout: Number, waitRetryInterval: Number, body: T.() -> R) = at(pageFactory(), waitTimeout, waitRetryInterval, body)
    fun <T : Page> at(page: T, waitTimeout: Number, waitRetryInterval: Number) = at(page, waitTimeout, waitRetryInterval) { page }
    fun <T : Page, R : Any> at(page: T, waitTimeout: Number, waitRetryInterval: Number, body: T.() -> R) = atInternal(page, WaitPreset(waitTimeout, waitRetryInterval), body)
    // wait preset wait
    fun <T : Page> at(pageFactory: () -> T, wait: WaitPreset) = pageFactory().let { at(it, wait, { it }) }
    fun <T : Page, R : Any> at(pageFactory: () -> T, wait: WaitPreset, body: T.() -> R) = at(pageFactory(), wait, body)
    fun <T : Page> at(page: T, wait: WaitPreset) = at(page, wait) { page }
    fun <T : Page, R : Any> at(page: T, wait: WaitPreset, body: T.() -> R) = atInternal(page, wait, body)

    // VIA
    fun <T : Page, R : Any> T.via(body: T.() -> R) = body(this)
    /**
     * @param classValidator Used just to explicitly declare type of body context. Useful for fluent api usage.
     */
    fun <T : Page, R : Any> T.via(classValidator: KClass<T>, body: T.() -> R) = body(this)

    // HELPERS
    private fun resolveUrl(urlSuffix: String): String {
        val urlPrefix = browser.baseUrl
        val url = if (urlPrefix.isEmpty()) urlSuffix else "$urlPrefix/$urlSuffix"
        return URI(url).normalize().toString()
    }
    private fun <T : Page, R : Any> atInternal(page: T, wait: Any, body: T.() -> R): R {
        page.browser = browser
        @Suppress("UNCHECKED_CAST")
        return body(page.verifyAt(wait) as T)
    }
}