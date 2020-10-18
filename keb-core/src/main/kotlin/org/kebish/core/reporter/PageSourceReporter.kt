package org.kebish.core.reporter

import org.kebish.core.browser.Browser
import org.kebish.core.config.TestInfo
import java.io.File

public class PageSourceReporter : ReportsDirReporter() {

    override fun report(testInfo: TestInfo, browser: Browser) {
        if (!browser.isDriverInitialized()) {
            return
        }

        val pageSourceFile = File(resolvedReportsDir(), testInfo.reportPath + ".html").canonicalFile

        pageSourceFile.parentFile.mkdirs()
        pageSourceFile.writeText(browser.pageSource)
    }

}