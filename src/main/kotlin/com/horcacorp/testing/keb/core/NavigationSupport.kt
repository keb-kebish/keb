package com.horcacorp.testing.keb.core

interface NavigationSupport {

    fun <T : Page> to(factory: (Browser) -> T): T

    fun <T : Page> at(factory: (Browser) -> T): T

}