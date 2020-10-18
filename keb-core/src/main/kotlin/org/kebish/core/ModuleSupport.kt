package org.kebish.core

import org.kebish.core.browser.Browser
import org.kebish.core.module.Module
import org.openqa.selenium.WebElement

public interface ModuleSupport {

    public val browser: Browser

    public fun <T : Module> module(factory: () -> T): T = module(factory())

    public fun <T : Module> module(module: T): T = module.apply { browser = this@ModuleSupport.browser }

    public fun <T : Module> WebElement.module(factory: (scope: WebElement) -> T): T = module(factory(this))

}