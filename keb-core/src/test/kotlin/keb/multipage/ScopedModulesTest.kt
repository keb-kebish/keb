package keb.multipage

import com.horcacorp.testing.keb.core.*
import io.github.bonigarcia.wdm.WebDriverManager
import keb.junit5.KebTest
import keb.test.util.HttpResourceFolderServer
import keb.test.util.WithHttpResourceFolderServer
import org.junit.jupiter.api.Test
import org.openqa.selenium.WebElement
import org.openqa.selenium.firefox.FirefoxDriver


class ScopedModulesTest : KebTest(Browser(kebConfig {
    WebDriverManager.firefoxdriver().setup()
    this.driver = FirefoxDriver()
})), WithHttpResourceFolderServer {

    override var server = HttpResourceFolderServer("keb/testing/multipage")

    @Test
    fun `navigation menu works`() {
        // given
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


    class LandingPage(browser: Browser) : Page(browser) {
        override fun url() = "/"
        override fun at() = header.text == "Landing page"

        val header by content { css("h1") }
        val menu by content { scopedModule(::NavigationMenuModule, css(".navigation_menu")) }
    }

    class Page1Page(browser: Browser) : Page(browser) {
        override fun url() = "/"
        override fun at() = header.text == "Page 1"

        val header by content { css("h1") }
        val menu by content { scopedModule(::NavigationMenuModule, css(".navigation_menu")) }
    }

    class Page2Page(browser: Browser) : Page(browser) {
        override fun url() = "/"
        override fun at() = header.text == "Page 2"

        val header by content { css("h1") }
        val menu by content { scopedModule(::NavigationMenuModule, css(".navigation_menu")) }
    }

    class NavigationMenuModule(browser: Browser, scope: WebElement) : ScopedModule(browser, scope) {
        val landingLink by content { css(".nav_landing") }
        val page1Link by content { css(".nav_page1") }
        val page2Link by content { css(".nav_page2") }
    }

}



