package org.kebish.core.browser.provider

import org.kebish.core.Browser
import org.kebish.core.Configuration
import org.kebish.core.util.ResettableLazy

class NewBrowserForEachTestProvider(val config: Configuration) : BrowserProvider {

    private val browserDelegate = ResettableLazy(
        initializer = { Browser(config) },
        onReset = { browser -> browser.quit() }
    )

    private val browser by browserDelegate


    override fun provideBrowser(): Browser {
        return browser
    }

    override fun beforeTest() {
    }

    override fun afterTest() {
        browserDelegate.reset()
    }
}