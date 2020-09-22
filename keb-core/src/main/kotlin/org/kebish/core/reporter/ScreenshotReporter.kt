package org.kebish.core.reporter

import org.kebish.core.browser.Browser
import org.kebish.core.config.TestInfo
import org.kebish.core.tool.ScreenshotMaker
import java.io.File

class ScreenshotReporter : ReportsDirReporter() {


    override fun report(testInfo: TestInfo, browser: Browser) {
        if (!browser.isDriverInitialized()) {
            return
        }
        val screenshotMaker = ScreenshotMaker(browser)
        val screenshotFile = File(resolvedReportsDir(), testInfo.reportPath + ".png").canonicalFile
        screenshotMaker.takeScreenshot(screenshotFile)
    }

}