package org.kebish.core

import org.openqa.selenium.NoSuchElementException
import kotlin.reflect.KProperty

/** T this is not nullable. I cannot see case, where we want return null */
class Content<T : Any?>(val cache: Boolean, val required: Boolean, val initializer: () -> T) {

    private var cachedValueInitialized = false
    private var cachedValue: T? = null

    operator fun getValue(thisRef: Any?, prop: KProperty<*>): T {
        if (cache) {
            if (!cachedValueInitialized) {
                cachedValue = checkedInitializer()
                cachedValueInitialized = true
            }
            return cachedValue!!
        } else {
            return checkedInitializer()
        }
    }

    private fun checkedInitializer(): T {
        val content = initializer()
        return if (required) {
            when(content) {
                is EmptyContent -> throw NoSuchElementException("Required page content is not present. Selector='${content.missingContentSelector}'.")
                null -> throw NoSuchElementException("Required page content is not present. Selector returned 'null'.")
                else -> content
            }
        } else {
            content
        }
    }
}

fun <T : Any?> content(cache: Boolean = false, required: Boolean = true, initializer: () -> T)
        = Content(cache, required, initializer)

interface EmptyContent {
    val missingContentSelector: String
}