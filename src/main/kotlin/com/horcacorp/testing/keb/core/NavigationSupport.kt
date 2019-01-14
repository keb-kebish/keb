package com.horcacorp.testing.keb.core

interface NavigationSupport {

    fun <T : Page> to(factory: (Browser) -> T, waitParam: Any? = null): T

    fun <T : Page> at(factory: (Browser) -> T, waitParam: Any? = null): T

}