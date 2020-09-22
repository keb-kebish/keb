package org.kebish.core.module

import org.kebish.core.ContentSupport
import org.kebish.core.ModuleSupport
import org.kebish.core.NavigationSupport
import org.kebish.core.WaitSupport
import org.kebish.core.browser.Browser
import org.openqa.selenium.WebElement

abstract class Module(val scope: WebElement? = null) : ContentSupport, ModuleSupport, NavigationSupport, WaitSupport {

    override lateinit var browser: Browser

    override fun getDefaultScope(): WebElement? {
        return scope
    }
}