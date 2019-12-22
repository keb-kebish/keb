package com.horcacorp.testing.keb.core

interface NavigationSupport {

    fun <T : Page> to(pageFactory: (Browser) -> T, waitPreset: String? = null, body: (T) -> Unit = {}): T
    fun <T : Page> at(factory: (Browser) -> T, waitPreset: String? = null): T
    fun <T> withNewTab(action: () -> T): T
    fun <T> withClosedTab(action: () -> T): T

}