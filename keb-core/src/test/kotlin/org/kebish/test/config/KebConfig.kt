package org.kebish.test.config

import io.github.bonigarcia.wdm.WebDriverManager
import org.kebish.core.config.kebConfig
import org.kebish.core.reporter.PageSourceReporter
import org.kebish.core.reporter.ScreenshotReporter
import org.openqa.selenium.firefox.FirefoxDriver
import java.io.File

fun commonTestKebConfig() = kebConfig {
    WebDriverManager.firefoxdriver().setup()
    this.driver = { FirefoxDriver() }
    reports.apply {
        reporterDir = File("build/keb-reports")
        testFailReporters.add(ScreenshotReporter())
        testFailReporters.add(PageSourceReporter())
    }
}