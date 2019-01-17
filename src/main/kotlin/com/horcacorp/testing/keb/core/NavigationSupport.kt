package com.horcacorp.testing.keb.core

import kotlin.reflect.KClass

interface NavigationSupport {

    fun <T : Page> to(factory: (Browser) -> T, waitParam: Any? = null): T
    fun <T : Page> to(klass: KClass<T>, waitParam: Any? = null): T
    fun <T : Page> at(factory: (Browser) -> T, waitParam: Any? = null): T
    fun <T : Page> at(klass: KClass<T>, waitParam: Any? = null): T
    fun <T> withNewTab(action: () -> T): T
    fun <T> withClosedTab(action: () -> T): T

}