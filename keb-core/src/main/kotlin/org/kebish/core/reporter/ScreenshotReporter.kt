package org.kebish.core.reporter

import org.kebish.core.Browser
import org.kebish.core.Configuration
import org.kebish.core.config.TestInfo
import org.kebish.core.tool.ScreenshotMaker
import java.io.File

class ScreenshotReporter : Configuration.Reporter {

    override fun report(testInfo: TestInfo, browser: Browser) {
        val screenshotMaker = ScreenshotMaker(browser)
        //TODO define reportDirectory
        val screenshotFile = File(testInfo.name + ".png").canonicalFile
        //TODO logger
        println("TAKING SCREENSHOT TO - '$screenshotFile'")
        screenshotMaker.takeScreenshot(screenshotFile)
        println("Done.")
    }
}