package org.kebish.core.browser.provider

import org.kebish.core.browser.Browser
import org.kebish.core.config.Configuration
import org.kebish.core.util.ResettableLazy

public class NewBrowserForEachTestProvider() : BrowserProvider {

    private val browserDelegate = ResettableLazy(
        initializer = { Browser(config) },
        onReset = { browser -> browser.quit() }
    )
    private lateinit var config: Configuration

    private val browser by browserDelegate


    override fun provideBrowser(config: Configuration): Browser {
        this.config = config
        browser.config = config
        return browser
    }

    override fun beforeTest() {
    }

    override fun afterTest() {
        browserDelegate.reset()
    }
}