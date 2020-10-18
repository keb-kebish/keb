package org.kebish.core.test

import org.kebish.core.ContentSupport
import org.kebish.core.ModuleSupport
import org.kebish.core.NavigationSupport
import org.kebish.core.WaitSupport
import org.kebish.core.browser.Browser
import org.kebish.core.config.Configuration
import org.kebish.core.config.TestInfo

/**
 * Implementation of keb test base
 */
public abstract class KebTestBase(public val config: Configuration) : ContentSupport, ModuleSupport, NavigationSupport,
    WaitSupport {

    override val browser: Browser
        get() = config.browserProvider.provideBrowser(config)

    /** Test runner must call this method before each test */
    public fun beforeEachTest() {
        config.browserProvider.beforeTest()
    }

    /** Test runner must call this method on test failure. Before afterTest() and finalizeTest() methods.  */
    public fun afterTestFail(testInfo: TestInfo) {
        config.reports.testFailReporters.forEach { reporter ->
            reporter.report(testInfo, browser)
        }
    }

    /** Test runner must call this method on test success. Before afterTest() and finalizeTest() methods.  */
    public fun afterTestSuccess(testInfo: TestInfo) {
        config.reports.testSuccessReporters.forEach { reporter ->
            reporter.report(testInfo, browser)
        }
    }

    /**
     * Test runner must call this method after test.
     * Before finalizeTest() method.
     * After afterTestFail/afterTestSuccess methods.
     */
    public fun afterTest(testInfo: TestInfo) {
        config.reports.afterEachTestReporters.forEach { reporter ->
            reporter.report(testInfo, browser)
        }
    }

    /** Test runner must call this method as LAST method after test*/
    public fun finalizeTest() {
        config.browserProvider.afterTest()
    }

    @Deprecated("Old name of method use 'finalizeTest()' method instead.", ReplaceWith("finalizeTest()"))
    public fun afterEachTest() {
        finalizeTest()
    }
}