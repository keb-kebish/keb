package com.horcacorp.testing.keb.core

abstract class Module(val browser: Browser) : ContentSupport by browser, NavigationSupport by browser,
    WaitSupport by browser, ModuleSupport by browser