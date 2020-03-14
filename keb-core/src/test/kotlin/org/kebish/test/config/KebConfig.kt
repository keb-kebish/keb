package org.kebish.test.config

import io.github.bonigarcia.wdm.WebDriverManager
import org.kebish.core.kebConfig
import org.openqa.selenium.firefox.FirefoxDriver

fun commonTestKebConfig() = kebConfig {
    WebDriverManager.firefoxdriver().setup()
    this.driver = { FirefoxDriver() }
}