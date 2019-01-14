package com.horcacorp.testing.keb.junit

import com.horcacorp.testing.keb.core.Browser
import com.horcacorp.testing.keb.core.NavigationSupport
import com.horcacorp.testing.keb.core.Page
import com.horcacorp.testing.keb.core.kebConfig
import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.After
import org.openqa.selenium.chrome.ChromeDriver

abstract class KebTest : NavigationSupport {

    private val browser = Browser(kebConfig {
        WebDriverManager.chromedriver().setup()
        driver = ChromeDriver()
    })

    override fun <T : Page> to(factory: (Browser) -> T, waitParam: Any?) = browser.to(factory, waitParam)

    override fun <T : Page> at(factory: (Browser) -> T, waitParam: Any?) = browser.at(factory, waitParam)

    @After
    fun closeDriver() {
        browser.quit()
    }

}