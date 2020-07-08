package org.kebish.core.reporter

import org.kebish.core.Browser
import org.kebish.core.config.TestInfo
import java.io.File

class PageSourceReporter : ReportsDirReporter() {

    override fun report(testInfo: TestInfo, browser: Browser) {
        if (!browser.isDriverInitialized()) {
            return
        }


        //TODO test that reporter dir is used maybe pull it out to Abstract parent


        val screenshotFile = File(resolvedReportsDir(), testInfo.name + ".html").canonicalFile

        screenshotFile.writeText(browser.driver.pageSource)

    }

}