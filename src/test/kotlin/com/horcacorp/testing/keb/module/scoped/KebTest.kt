package com.horcacorp.testing.keb.module.scoped

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
        val driver = FirefoxDriver()
        this.driver = driver
        baseUrl = javaClass.classLoader.getResource("com/horcacorp/testing/keb/module/scoped/page.html")!!.toString()
            .replace("page.html", "")
    })

    @AfterEach
    fun closeDriver() {
        browser.quit()
    }

    // delegate navigation to browser
    override fun <T : Page> to(pageFactory: (Browser) -> T, waitPreset: String?, body: T.() -> Unit): T =
        browser.to(pageFactory, waitPreset, body)

    override fun <T : Page> at(pageFactory: (Browser) -> T, waitPreset: String?, body: T.() -> Unit): T =
        browser.at(pageFactory, waitPreset, body)

    override fun <T> withNewTab(action: () -> T): T = browser.withNewTab(action)
    override fun <T> withClosedTab(action: () -> T): T = browser.withClosedTab(action)

}