package org.kebish.core.reporter

import org.kebish.core.Browser
import org.kebish.core.config.TestInfo
import org.kebish.core.tool.ScreenshotMaker
import java.io.File

class ScreenshotReporter : ReportsDirReporter() {


    override fun report(testInfo: TestInfo, browser: Browser) {
        if (!browser.isDriverInitialized()) {
            return
        }

        val screenshotMaker = ScreenshotMaker(browser)
        //TODO test that reporter dir is used maybe pull it out to Abstract parent


        val screenshotFile = File(resolvedReportsDir(), testInfo.name + ".png").canonicalFile
        //TODO logger
        println("TAKING SCREENSHOT TO - '$screenshotFile'")
        screenshotMaker.takeScreenshot(screenshotFile)
        println("Done.")
    }

}