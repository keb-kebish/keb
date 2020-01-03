package com.horcacorp.testing.keb.core

import org.openqa.selenium.WebElement

abstract class Module(val browser: Browser, val scope: WebElement? = null) : NavigationSupport by browser,
    WaitSupport by browser, ModuleSupport by browser