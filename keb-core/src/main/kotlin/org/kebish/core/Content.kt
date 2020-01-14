package org.kebish.core

import kotlin.reflect.KProperty

/** T this is not nullable. I cannot see case, where we want return null */
class Content<T : Any>(val cache: Boolean = false, val initializer: () -> T) {

    private lateinit var cachedValue: T

    operator fun getValue(thisRef: Any?, prop: KProperty<*>): T {
        if (cache) {
            if (!::cachedValue.isInitialized) {
                cachedValue = initializer()
            }
            return cachedValue
        } else {
            return initializer()
        }
//            return "$thisRef, thank you for delegating '${prop.name}' to me!"
    }
}

fun <T : Any> content(cache: Boolean = false, initializer: () -> T) = Content(cache, initializer)
