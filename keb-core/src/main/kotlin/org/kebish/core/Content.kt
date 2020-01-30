package org.kebish.core

import org.openqa.selenium.NoSuchElementException
import kotlin.reflect.KProperty

class Content<T>(private val contentInitializer: ContentInitializer<T>) {
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
    private val wait: Boolean,
    override val browser: Browser,
    private val decorated: ContentInitializer<T>
) : ContentInitializer<T>, WaitSupport {
    override fun initialize() = if (wait) waitFor(f = decorated::initialize) else decorated.initialize()
}

class WaitingByTimeoutContentInitializer<T>(
    private val timeout: Number,
    override val browser: Browser,
    private val decorated: ContentInitializer<T>
) : ContentInitializer<T>, WaitSupport {
    override fun initialize() = waitFor(timeout = timeout, f = decorated::initialize)
}

class WaitingByTimeoutAndRetryIntervalContentInitializer<T>(
    private val timeout: Number,
    private val retryInterval: Number,
    override val browser: Browser,
    private val decorated: ContentInitializer<T>
) : ContentInitializer<T>, WaitSupport {
    override fun initialize() = waitFor(timeout = timeout, retryInterval = retryInterval, f = decorated::initialize)
}

class WaitingByPresetNameContentInitializer<T>(
    private val waitPresetName: String,
    override val browser: Browser,
    private val decorated: ContentInitializer<T>
) : ContentInitializer<T>, WaitSupport {
    override fun initialize() = waitFor(preset = waitPresetName, f = decorated::initialize)
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

interface EmptyContent {
    val missingContentSelector: String
}