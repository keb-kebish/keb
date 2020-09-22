package org.kebish.core

import org.kebish.core.browser.Browser
import org.kebish.core.module.Module
import org.openqa.selenium.WebElement

interface ModuleSupport {

    val browser: Browser

    fun <T : Module> module(factory: () -> T) = module(factory())

    fun <T : Module> module(module: T) = module.apply { browser = this@ModuleSupport.browser }

    fun <T : Module> WebElement.module(factory: (scope: WebElement) -> T) = module(factory(this))

}