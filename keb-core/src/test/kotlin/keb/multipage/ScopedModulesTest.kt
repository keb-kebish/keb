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
        to(::HomePage) {
            menu.contactsLink.click()
        }

        at(::ContactsPage) {
            menu.aboutLink.click()
        }

        at(::AboutPage)
    }

}

class HomePage(browser: Browser) : Page(browser) {
    override fun url() = "/"
    override fun at() = header.text == "Welcome"

    val menu by content { NavigationMenuModule(browser, css(".navigation_menu")) }
    val header by content { css("h1") }
}

class AboutPage(browser: Browser) : Page(browser) {
    override fun url() = "about.html"
    override fun at() = header.text == "About us"

    val menu by content { NavigationMenuModule(browser, css(".navigation_menu")) }
    val header by content { css("h1") }
}

class ContactsPage(browser: Browser) : Page(browser) {
    override fun url() = "contacts.html"
    override fun at() = header.text == "Contacts"

    val menu by content { NavigationMenuModule(browser, css(".navigation_menu")) }
    val header by content { css("h1") }
}

class NavigationMenuModule(browser: Browser, scope: WebElement) : Module(browser, scope) {
    val homeLink by content { css(".nav_welcome") }
    val aboutLink by content { css(".nav_about") }
    val contactsLink by content { css(".nav_contacts") }

    fun toHomePage(): HomePage {
        homeLink.click()
        return at(::HomePage)
    }
}
