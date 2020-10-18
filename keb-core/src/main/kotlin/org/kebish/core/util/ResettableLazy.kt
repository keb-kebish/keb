package org.kebish.core.util

import kotlin.reflect.KProperty

internal class ResettableLazy<T : Any>(val onReset: (T) -> Unit, val initializer: () -> T) {

    private var value: T? = null

    operator fun getValue(thisRef: Any?, prop: KProperty<*>): T {
        if (value == null) {
            value = initializer()
        }
        return value as T
    }

    fun reset() {
        if (isInitialized()) {
            val valueToReset = value
            value = null
            onReset(valueToReset!!)
        }
    }

    fun isInitialized(): Boolean {
        return value != null
    }

}

