package org.kebish.core.reporter

import org.kebish.core.Browser
import org.kebish.core.Configuration
import org.kebish.core.config.TestInfo
import org.kebish.core.tool.ScreenshotMaker
import java.io.File

class ScreenshotReporter : Configuration.Reporter {

    private lateinit var reportsConfig: Configuration.Reports

    override fun report(testInfo: TestInfo, browser: Browser) {
        val screenshotMaker = ScreenshotMaker(browser)
        //TODO test that reporter dir is used maybe pull it out to Abstract parent
        val reportsDir = if (::reportsConfig.isInitialized) {
            reportsConfig.reporterDir
        } else {
            File("")
        }

        val screenshotFile = File(reportsDir, testInfo.name + ".png").canonicalFile
        //TODO logger
        println("TAKING SCREENSHOT TO - '$screenshotFile'")
        screenshotMaker.takeScreenshot(screenshotFile)
        println("Done.")
    }

    override fun setConfig(reports: Configuration.Reports) {
        this.reportsConfig = reports
    }
}