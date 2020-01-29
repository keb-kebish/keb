package org.kebish.core

import org.openqa.selenium.NoSuchElementException
import kotlin.reflect.KProperty

class Content<T : Any?>(val cache: Boolean, val required: Boolean, val initializer: () -> T) {

    private var cachedValue: Any? = UNINITIALIZED_VALUE

    operator fun getValue(thisRef: Any?, prop: KProperty<*>): T {
        if (cache) {
            if (cachedValue === UNINITIALIZED_VALUE) {
                cachedValue = checkedInitializer()
            }
            @Suppress("UNCHECKED_CAST")
            return cachedValue as T
        } else {
            return checkedInitializer()
        }
    }

    private fun checkedInitializer(): T {
        val content = initializer()
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

fun <T : Any?> content(cache: Boolean = false, required: Boolean = true, initializer: () -> T) =
    Content(cache, required, initializer)

interface EmptyContent {
    val missingContentSelector: String
}

internal object UNINITIALIZED_VALUE