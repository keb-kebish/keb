package com.horcacorp.testing.keb.core

abstract class Module : NavigationSupport {

    lateinit var _browser: Browser

    override fun getBrowser(): Browser {
        return _browser
    }

}

class MyModule : Module() {

    init {
        to()
    }

}