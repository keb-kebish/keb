package com.horcacorp.testing.keb.core

import org.openqa.selenium.WebElement

interface ModuleSupport {

    fun <T : Module> module(factory: (Browser, WebElement?) -> T, scope: WebElement? = null): T

}