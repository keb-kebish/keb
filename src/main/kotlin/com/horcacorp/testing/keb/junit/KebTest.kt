package com.horcacorp.testing.keb.junit

import com.horcacorp.testing.keb.core.Browser
import com.horcacorp.testing.keb.core.NavigationSupport
import com.horcacorp.testing.keb.core.Page
import com.horcacorp.testing.keb.core.kebConfig
import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.After
import org.openqa.selenium.chrome.ChromeDriver
import kotlin.reflect.KClass

abstract class KebTest : NavigationSupport {

    val browser = Browser(kebConfig {
        WebDriverManager.chromedriver().setup()
        driver = ChromeDriver()
    })

    @After
    fun closeDriver() {
        browser.quit()
    }

    // delegate navigation to browser
    override fun <T : Page> to(factory: (Browser) -> T, waitParam: Any?): T = browser.to(factory, waitParam)
    override fun <T : Page> to(klass: KClass<T>, waitParam: Any?): T = browser.to(klass, waitParam)
    override fun <T : Page> at(factory: (Browser) -> T, waitParam: Any?): T = browser.at(factory, waitParam)
    override fun <T : Page> at(klass: KClass<T>, waitParam: Any?): T = browser.at(klass, waitParam)
    override fun <T> withNewTab(action: () -> T): T  = browser.withNewTab(action)
    override fun <T> withClosedTab(action: () -> T): T = browser.withClosedTab(action)

}