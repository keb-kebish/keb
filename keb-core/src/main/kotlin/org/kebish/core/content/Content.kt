@file:Suppress("UNCHECKED_CAST", "ClassName")

package org.kebish.core.content

import org.kebish.core.ContentSupport
import org.kebish.core.WaitPresetFactory
import org.kebish.core.browser.Browser
import org.openqa.selenium.NoSuchElementException
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

public class Content<T : Any?>(private val contentInitializer: ContentInitializer<T>) :
    ReadOnlyProperty<ContentSupport, T> {
    override operator fun getValue(thisRef: ContentSupport, property: KProperty<*>): T {
        return contentInitializer.initialize(thisRef.browser)
    }
}

public interface ContentInitializer<T : Any?> {
    public fun initialize(browser: Browser): T
}

public class CachingContentInitializer<T : Any?>(
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


public class WaitingContentInitializer<T : Any?>(
    private val waitConfig: Any,
    private val decorated: ContentInitializer<T>
) : ContentInitializer<T> {
    override fun initialize(browser: Browser): T {
        return WaitPresetFactory().from(waitConfig, browser.config)
            ?.let { browser.waitFor(it) { decorated.initialize(browser) } }
            ?: decorated.initialize(browser)
    }
}

public class RequiredCheckingContentInitializer<T : Any?>(
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

public class ContentProvidingInitializer<T>(private val initializer: () -> T) : ContentInitializer<T> {
    override fun initialize(browser: Browser): T {
        return initializer()
    }
}

public interface EmptyContent {
    public val missingContentSelector: String
}