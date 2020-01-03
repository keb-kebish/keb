package com.horcacorp.testing.keb.core

interface NavigationSupport {

    fun <T : Page> to(pageFactory: () -> T, waitPreset: String? = null, body: (T) -> Unit = {}): T
    fun <T : Page> to(page: T, waitPreset: String? = null, body: (T) -> Unit = {}): T
    fun <T : Page> at(pageFactory: () -> T, waitPreset: String? = null, body: (T) -> Unit = {}): T
    fun <T : Page> at(page: T, waitPreset: String? = null, body: (T) -> Unit = {}): T

}