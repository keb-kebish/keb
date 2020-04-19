package org.kebish.core.test

import org.kebish.core.*

/**
 * Implementation of keb test base
 */
abstract class KebTestBase(val config: Configuration) : ContentSupport, ModuleSupport, NavigationSupport, WaitSupport {

    init {
        browser.config = config
    }

    override val browser: Browser
        get() = config.browserProvider.provideBrowser() //TODO maybe config could go there in this direction instead of constructor


    /** Test runner must call this method before each test */
    fun beforeEachTest() {
        config.browserProvider.beforeTest()
    }

    /** Test runner must call this method after each test */
    fun afterEachTest() {
        config.browserProvider.afterTest()
    }


}