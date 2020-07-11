package org.kebish.core.browser.management

import org.junit.jupiter.api.Test
import org.kebish.core.Browser
import org.kebish.core.browser.provider.NewBrowserForEachTestProvider
import org.kebish.core.config.TestInfo
import org.kebish.core.kebConfig
import org.kebish.core.reporter.ReportsDirReporter
import org.kebish.junit5.KebTest

class FakeReproter : ReportsDirReporter() {
    override fun report(testInfo: TestInfo, browser: Browser) {
    }
}

class DoNotStartBrowserIfNotNeeded_Reporters_Test : KebTest(kebConfig {
    driver = {
        throw IllegalStateException("TEST FAILED - Browser Is not needed for this test")
    }
    browserProvider = NewBrowserForEachTestProvider()
    reports.apply {
        testSuccessReporters.add(FakeReproter())
        testFailReporters.add(FakeReproter())
        afterEachTestReporters.add(FakeReproter())
    }
}) {

    @Test
    fun `reporters do not start driver if not needed`() {
        println(
            "Assert - When browser is not used, then no browser is initialized.\n" +
                    "If test try to create browser, exception will be thrown and this test fails."
        )
    }


}