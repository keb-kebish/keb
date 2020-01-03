package keb.multipage

import com.horcacorp.testing.keb.core.*
import io.github.bonigarcia.wdm.WebDriverManager
import keb.junit5.KebTest
import keb.test.util.HttpResourceFolderServer
import keb.test.util.WithHttpResourceFolderServer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openqa.selenium.WebElement
import org.openqa.selenium.firefox.FirefoxDriver


interface Extension {
    fun beforeEach()
}

interface Extendable {
    val extensions: MutableList<Extension>

    fun register(e:Extension) {
        extensions.add(e)
    }

    @BeforeEach
    fun beforeAach() {
        extensions.forEach { it.beforeEach() }
    }
}

typealias xxx = Extendable

class ExtendableImpl:Extendable {
    override val extensions = mutableListOf<Extension>()
}

class MyExtension: Extension {
    override fun beforeEach() {
        println("BEFEORE")
    }
}

class ScopedModulesTest : KebTest(Browser(kebConfig {
    WebDriverManager.firefoxdriver().setup()
    this.driver = FirefoxDriver()
})), WithHttpResourceFolderServer, Extendable by ExtendableImpl() {

    override var server = HttpResourceFolderServer("keb/testing/multipage")

    init {
        register(MyExtension())
    }


    @Test
    fun `navigation menu works`() {
        val browser2 = addBrowser(new ChromeDriver())

        switchBrowser(browser2)

        withc(Browser2)
        // given
        val ladn = to(::LandingPage, browser2) {

            // when
            menu.page2Link.click()
        }

        // then
        at(Page2Page()) {

            // when
            menu.page1Link.click()
        }

        // then
        at(::Page1Page)


        this to2 Page2Page()



          var x = 0
        ++ x
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




infix operator fun KebTest.to2(page: Page) = this.browser.to(page)

operator fun KebTest.inc(): Page     = this.browser.to()