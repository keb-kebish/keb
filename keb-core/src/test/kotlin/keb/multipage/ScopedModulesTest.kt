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


class ScopedModulesTest : KebTest(Browser(kebConfig {
    WebDriverManager.firefoxdriver().setup()
    this.driver = FirefoxDriver()
})) {

    @Test
    fun `resource dir server works`() {
        val server = HttpResourceFolderServer("keb/testing/multipage")
        browser.config.baseUrl = "http://localhost:${server.port}/"

        to(::LandingPage) {
            menu.page2Link.click()
        }

        at(::Page2Page) {
            menu.page1Link.click()
        }

        at(::Page1Page)

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



