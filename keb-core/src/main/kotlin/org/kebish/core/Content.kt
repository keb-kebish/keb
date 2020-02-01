@file:Suppress("UNCHECKED_CAST", "ClassName")

package org.kebish.core

import org.openqa.selenium.NoSuchElementException
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class Content<T : Any?>(private val contentInitializer: ContentInitializer<T>) : ReadOnlyProperty<ContentSupport, T> {
    override operator fun getValue(thisRef: ContentSupport, property: KProperty<*>): T {
        return contentInitializer.initialize(thisRef.browser)
    }
}

interface ContentInitializer<T : Any?> {
    fun initialize(browser: Browser): T
}

class CachingContentInitializer<T : Any?>(
    private val cache: Boolean,
    private val decorated: ContentInitializer<T>
) : ContentInitializer<T> {
    private object UNINITIALIZED_VALUE
    private var cachedValue: Any? = UNINITIALIZED_VALUE
    override fun initialize(browser: Browser): T {
        return if (cache) {
            if (cachedValue === UNINITIALIZED_VALUE) {
                cachedValue = decorated.initialize(browser)
            }
            cachedValue as T
        } else {
            decorated.initialize(browser)
        }
    }
}


class WaitingContentInitializer<T : Any?>(
    private val wait: WaitConfig,
    private val decorated: ContentInitializer<T>
) : ContentInitializer<T> {
    override fun initialize(browser: Browser) = browser.waitFor(config = wait) { decorated.initialize(browser) } as T
}

class RequiredCheckingContentInitializer<T : Any?>(
    private val required: Boolean,
    private val decorated: ContentInitializer<T>
) : ContentInitializer<T> {
    override fun initialize(browser: Browser): T {
        val content = decorated.initialize(browser)
        return if (required) {
            when (content) {
                is EmptyContent -> throw NoSuchElementException("Required page content is not present. Selector='${content.missingContentSelector}'.")
                null -> throw NoSuchElementException("Required page content is not present. Selector returned 'null'.")
                else -> content
            }
        } else {
            content
        }
    }
}

class ContentProvidingInitializer<T>(private val initializer: () -> T) : ContentInitializer<T> {
    override fun initialize(browser: Browser): T {
        return initializer()
    }
}

interface EmptyContent {
    val missingContentSelector: String
}