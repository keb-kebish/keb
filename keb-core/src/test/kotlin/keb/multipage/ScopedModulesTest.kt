package keb.multipage

import com.horcacorp.testing.keb.core.Browser
import com.horcacorp.testing.keb.core.Page
import com.horcacorp.testing.keb.core.ScopedModule
import com.horcacorp.testing.keb.core.kebConfig
import io.github.bonigarcia.wdm.WebDriverManager
import keb.junit5.KebTest
import keb.test.util.HttpResourceFolderServer
import org.junit.jupiter.api.Test
import org.openqa.selenium.WebElement
import org.openqa.selenium.firefox.FirefoxDriver
import kotlin.LazyThreadSafetyMode.NONE
import kotlin.reflect.KProperty


class ScopedModulesTest : KebTest(Browser(kebConfig {
    WebDriverManager.firefoxdriver().setup()
    this.driver = FirefoxDriver()
})) {

    @Test
    fun `navigation menu works`() {
        // given
        HttpResourceFolderServer("keb/testing/multipage").with { server ->
            browser.config.baseUrl = server.baseUrl
            to(::LandingPage) {

                // when
                menu.page2Link.click()
            }

            // then
            at(::Page2Page) {

                // when
                menu.page1Link.click()
            }

            // then
            at(::Page1Page)
        }
    }


    class LandingPage(browser: Browser) : Page(browser) {
        override fun url() = "/"
        override fun at() = header.text == "Landing page"

        val header = css("h1")
        val menu = scopedModule(::NavigationMenuModule, css(".navigation_menu"))
    }

    class Page1Page(browser: Browser) : Page(browser) {
        override fun url() = "/"
        override fun at() = header.text == "Page 1"

        val header = css("h1")
        val menu = scopedModule(::NavigationMenuModule, css(".navigation_menu"))
    }

    class Page2Page(browser: Browser) : Page(browser) {
        override fun url() = "/"
        override fun at() = header.text == "Page 2"

        val header = css("h1")
        val menu = scopedModule(::NavigationMenuModule, css(".navigation_menu"))
    }

    class NavigationMenuModule(browser: Browser, scope: WebElement) : ScopedModule(browser, scope) {
        val landingLink = css(".nav_landing")
        val page1Link = css(".nav_page1")
        val page2Link = css(".nav_page2")
    }

}



