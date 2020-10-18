package org.kebish.core

import org.kebish.core.browser.Browser
import org.kebish.core.page.Page
import java.net.URI
import kotlin.reflect.KClass

public interface NavigationSupport {

    public val browser: Browser

    // TO
    // boolean wait
    public fun <T : Page> to(pageFactory: () -> T, wait: Boolean = true): T = pageFactory().let { to(it, wait, { it }) }
    public fun <T : Page, R : Any> to(pageFactory: () -> T, wait: Boolean = true, body: T.() -> R): R =
        to(pageFactory(), wait, body)

    public fun <T : Page> to(page: T, wait: Boolean = true): T = to(page, wait, { page })
    public fun <T : Page, R : Any> to(page: T, wait: Boolean = true, body: T.() -> R): R =
        browser.driver.get(resolveUrl(page.url())).run { at(page, wait, body) }

    // string wait
    public fun <T : Page> to(pageFactory: () -> T, wait: String): T = pageFactory().let { to(it, wait, { it }) }
    public fun <T : Page, R : Any> to(pageFactory: () -> T, wait: String, body: T.() -> R): R =
        to(pageFactory(), wait, body)

    public fun <T : Page> to(page: T, wait: String): T = to(page, wait, { page })
    public fun <T : Page, R : Any> to(page: T, wait: String, body: T.() -> R): R =
        browser.driver.get(resolveUrl(page.url())).run { at(page, wait, body) }

    // number wait
    public fun <T : Page> to(pageFactory: () -> T, wait: Number): T = pageFactory().let { to(it, wait, { it }) }
    public fun <T : Page, R : Any> to(pageFactory: () -> T, wait: Number, body: T.() -> R): R =
        to(pageFactory(), wait, body)

    public fun <T : Page> to(page: T, wait: Number): T = to(page, wait, { page })
    public fun <T : Page, R : Any> to(page: T, wait: Number, body: T.() -> R): R =
        browser.driver.get(resolveUrl(page.url())).run { at(page, wait, body) }

    // timeout + retry interval wait
    public fun <T : Page> to(pageFactory: () -> T, waitTimeout: Number, waitRetryInterval: Number): T =
        pageFactory().let { to(it, waitTimeout, waitRetryInterval, { it }) }

    public fun <T : Page, R : Any> to(
        pageFactory: () -> T,
        waitTimeout: Number,
        waitRetryInterval: Number,
        body: T.() -> R
    ): R = to(pageFactory(), waitTimeout, waitRetryInterval, body)

    public fun <T : Page> to(page: T, waitTimeout: Number, waitRetryInterval: Number): T =
        to(page, waitTimeout, waitRetryInterval, { page })

    public fun <T : Page, R : Any> to(page: T, waitTimeout: Number, waitRetryInterval: Number, body: T.() -> R): R =
        browser.driver.get(resolveUrl(page.url())).run { at(page, waitTimeout, waitRetryInterval, body) }

    // wait preset wait
    public fun <T : Page> to(pageFactory: () -> T, wait: WaitPreset): T = pageFactory().let { to(it, wait, { it }) }
    public fun <T : Page, R : Any> to(pageFactory: () -> T, wait: WaitPreset, body: T.() -> R): R =
        to(pageFactory(), wait, body)

    public fun <T : Page> to(page: T, wait: WaitPreset): T = to(page, wait, { page })
    public fun <T : Page, R : Any> to(page: T, wait: WaitPreset, body: T.() -> R): R =
        browser.driver.get(resolveUrl(page.url())).run { at(page, wait, body) }

    // AT
    // boolean wait
    public fun <T : Page> at(pageFactory: () -> T, wait: Boolean = true): T = pageFactory().let { at(it, wait, { it }) }
    public fun <T : Page, R : Any> at(pageFactory: () -> T, wait: Boolean = true, body: T.() -> R): R =
        at(pageFactory(), wait, body)

    public fun <T : Page> at(page: T, wait: Boolean = true): T = at(page, wait) { page }
    public fun <T : Page, R : Any> at(page: T, wait: Boolean = true, body: T.() -> R): R = atInternal(page, wait, body)

    // string wait
    public fun <T : Page> at(pageFactory: () -> T, wait: String): T = pageFactory().let { at(it, wait, { it }) }
    public fun <T : Page, R : Any> at(pageFactory: () -> T, wait: String, body: T.() -> R): R =
        at(pageFactory(), wait, body)

    public fun <T : Page> at(page: T, wait: String): T = at(page, wait) { page }
    public fun <T : Page, R : Any> at(page: T, wait: String, body: T.() -> R): R = atInternal(page, wait, body)

    // number wait
    public fun <T : Page> at(pageFactory: () -> T, wait: Number): T = pageFactory().let { at(it, wait, { it }) }
    public fun <T : Page, R : Any> at(pageFactory: () -> T, wait: Number, body: T.() -> R): R =
        at(pageFactory(), wait, body)

    public fun <T : Page> at(page: T, wait: Number): T = at(page, wait) { page }
    public fun <T : Page, R : Any> at(page: T, wait: Number, body: T.() -> R): R = atInternal(page, wait, body)

    // timeout + retry interval wait
    public fun <T : Page> at(pageFactory: () -> T, waitTimeout: Number, waitRetryInterval: Number): T =
        pageFactory().let { at(it, waitTimeout, waitRetryInterval, { it }) }

    public fun <T : Page, R : Any> at(
        pageFactory: () -> T,
        waitTimeout: Number,
        waitRetryInterval: Number,
        body: T.() -> R
    ): R = at(pageFactory(), waitTimeout, waitRetryInterval, body)

    public fun <T : Page> at(page: T, waitTimeout: Number, waitRetryInterval: Number): T =
        at(page, waitTimeout, waitRetryInterval) { page }

    public fun <T : Page, R : Any> at(page: T, waitTimeout: Number, waitRetryInterval: Number, body: T.() -> R): R =
        atInternal(page, WaitPreset(waitTimeout, waitRetryInterval), body)

    // wait preset wait
    public fun <T : Page> at(pageFactory: () -> T, wait: WaitPreset): T = pageFactory().let { at(it, wait, { it }) }
    public fun <T : Page, R : Any> at(pageFactory: () -> T, wait: WaitPreset, body: T.() -> R): R =
        at(pageFactory(), wait, body)

    public fun <T : Page> at(page: T, wait: WaitPreset): T = at(page, wait) { page }
    public fun <T : Page, R : Any> at(page: T, wait: WaitPreset, body: T.() -> R): R = atInternal(page, wait, body)

    // VIA
    public fun <T : Page, R : Any> T.via(body: T.() -> R): R = body(this)

    /**
     * @param classValidator Used just to explicitly declare type of body context. Useful for fluent api usage.
     */
    public fun <T : Page, R : Any> T.via(classValidator: KClass<T>, body: T.() -> R): R = body(this)

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