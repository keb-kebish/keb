package org.kebish.core

import org.openqa.selenium.NoSuchElementException
import kotlin.reflect.KProperty

class Content<T : Any?>(private val contentInitializer: ContentInitializer<T>) {
    operator fun getValue(thisRef: Any?, prop: KProperty<*>): T {
        return contentInitializer.initialize()
    }
}

interface ContentInitializer<T> {
    fun initialize(): T
}

class CachingContentInitializer<T>(
    private val cache: Boolean,
    private val decorated: ContentInitializer<T>
) : ContentInitializer<T> {
    private object UNINITIALIZED_VALUE
    private var cachedValue: Any? = UNINITIALIZED_VALUE
    override fun initialize(): T {
        if (cache) {
            if (cachedValue === UNINITIALIZED_VALUE) {
                cachedValue = decorated.initialize()
            }
            @Suppress("UNCHECKED_CAST")
            return cachedValue as T
        } else {
            return decorated.initialize()
        }
    }
}

class WaitingContentInitializer<T>(
    private val wait: Any,
    override val browser: Browser,
    private val decorated: ContentInitializer<T>
) : ContentInitializer<T>, WaitSupport {
    override fun initialize(): T {
        return when (wait) {
            is Boolean -> if (wait) waitFor { decorated.initialize() } else decorated.initialize()
            is String -> waitFor(preset = wait) { decorated.initialize() }
            is Number -> waitFor(timeout = wait) { decorated.initialize() }
            is WaitPreset -> waitFor(timeout = wait.timeout, retryInterval = wait.retryInterval) { decorated.initialize() }
            else -> decorated.initialize()
        }
    }
}

class RequiredCheckingContentInitializer<T>(
    private val required: Boolean,
    private val decorated: ContentInitializer<T>
) : ContentInitializer<T> {
    override fun initialize(): T {
        val content = decorated.initialize()
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
    override fun initialize(): T {
        return initializer()
    }
}

fun <T : Any?> Page.content(
    required: Boolean = true,
    cache: Boolean = false,
    wait: Any = false,
    initializer: () -> T
) = Content(
    CachingContentInitializer(
        cache,
        WaitingContentInitializer(
            wait,
            browser,
            RequiredCheckingContentInitializer(
                required,
                ContentProvidingInitializer(initializer)
            )
        )
    )
)

fun <T : Any?> Module.content(
    required: Boolean = true,
    cache: Boolean = false,
    wait: Any = false,
    initializer: () -> T
) = Content(
    CachingContentInitializer(
        cache,
        WaitingContentInitializer(
            wait,
            browser,
            RequiredCheckingContentInitializer(
                required,
                ContentProvidingInitializer(initializer)
            )
        )
    )
)

interface EmptyContent {
    val missingContentSelector: String
}