package com.horcacorp.testing.keb

import com.horcacorp.testing.keb.core.Browser
import com.horcacorp.testing.keb.core.NavigationSupport
import com.horcacorp.testing.keb.core.Page
import com.horcacorp.testing.keb.core.kebConfig
import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.jupiter.api.AfterEach
import org.openqa.selenium.firefox.FirefoxDriver

abstract class KebTest : NavigationSupport {

    val browser = Browser(kebConfig {
        WebDriverManager.firefoxdriver().setup()
        val ffDriver = FirefoxDriver()
        driver = ffDriver
    })

    @AfterEach
    fun closeDriver() {
        browser.quit()
    }

    // delegate navigation to browser
    override fun <T : Page> to(factory: (Browser) -> T, waitPreset: String?): T = browser.to(factory, waitPreset)
    override fun <T : Page> at(factory: (Browser) -> T, waitPreset: String?): T = browser.at(factory, waitPreset)
    override fun <T> withNewTab(action: () -> T): T  = browser.withNewTab(action)
    override fun <T> withClosedTab(action: () -> T): T = browser.withClosedTab(action)

}