package com.horcacorp.testing.keb.core

abstract class Module(private val browser: Browser) : ContentSupport by browser, NavigationSupport by browser,
    WaitSupport by browser