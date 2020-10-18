package org.kebish.core.tool

import org.kebish.core.browser.Browser
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import java.io.File

public class ScreenshotMaker(public val browser: Browser) {

    public fun takeScreenshot(destination: File) {
        val screenshotDriver = determineScreenshotDriver(browser)


        var decoded = screenshotDriver.getScreenshotAs(OutputType.BYTES)
        destination.parentFile.mkdirs()
        destination.writeBytes(decoded)

    }


    private fun determineScreenshotDriver(browser: Browser): TakesScreenshot {
        val driver = browser.driver
        if (driver is TakesScreenshot) {
            return driver
        }

        throw IllegalStateException("Cannot take screenshot. Driver is not instance of 'org.openqa.selenium.TakesScreenshot'")
    }


}