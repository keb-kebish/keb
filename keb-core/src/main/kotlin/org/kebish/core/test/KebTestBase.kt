package org.kebish.core.test

import org.kebish.core.*

/**
 * Implementation of keb test base
 */
abstract class KebTestBase(val config: Configuration) : ContentSupport, ModuleSupport, NavigationSupport, WaitSupport {

    override val browser: Browser
        get() = config.browserProvider.provideBrowser()


    /** Test runner must call this method before each test */
    fun beforeEachTest() {
        config.browserProvider.beforeTest()
    }

    /** Test runner must call this method after each test */
    fun afterEachTest() {
        config.browserProvider.afterTest()
    }


}