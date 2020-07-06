package org.kebish.core.test

import org.kebish.core.*
import org.kebish.core.config.TestInfo

/**
 * Implementation of keb test base
 */
abstract class KebTestBase(val config: Configuration) : ContentSupport, ModuleSupport, NavigationSupport, WaitSupport {

    override val browser: Browser
        get() = config.browserProvider.provideBrowser(config) //TODO maybe config could go there in this direction instead of constructor


    /** Test runner must call this method before each test */
    fun beforeEachTest() {
        config.browserProvider.beforeTest()
    }


//TODO clean this up
//    fun afterTestSuccess() {
//
//    }


    /** Test runner must call this method on test failure. Before finalizeTest() method.  */
    fun afterTestFail(testInfo: TestInfo) {
        config.reports.testFailReporters.forEach { reporter ->
            reporter.report(testInfo, browser)
        }
    }


    /** Test runner must call this method as LAST method after test*/
    fun finalizeTest() {
        config.browserProvider.afterTest()
    }

    @Deprecated("Old name of method use 'finalizeTest()' method instead.", ReplaceWith("finalizeTest()"))
    fun afterEachTest() {
        finalizeTest()
    }
}