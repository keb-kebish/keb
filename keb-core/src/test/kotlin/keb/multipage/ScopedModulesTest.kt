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

    @Test
    fun `navigation menu works style2`() {
        val homePage = to(::HomePage)
        homePage.menu.contactsLink.click()

        val contactsPage = at(::ContactsPage)
        contactsPage.menu.aboutLink.click()

        at(::AboutPage)

        // Simple and strait forward,
        // but page can be used even when browser is on different page when you are already
    }

    @Test
    fun `navigation menu works style3`() {
        to(::HomePage).run {
            menu.toContacts()
        }.run {
            menu.toAbout()
        }
    }

}

class HomePage : Page() {
    override fun url() = "/"
    override fun at() = header.text == "Welcome"

    val menu by content { module(NavigationMenuModule(css(".navigation_menu"))) }
    val header by content { css("h1") }
}

class AboutPage : Page() {
    override fun url() = "about.html"
    override fun at() = header.text == "About us"

    val menu by content { module(NavigationMenuModule(css(".navigation_menu"))) }
    val header by content { css("h1") }
}

class ContactsPage : Page() {
    override fun url() = "contacts.html"
    override fun at() = header.text == "Contacts"

    val menu by content { module(NavigationMenuModule(css(".navigation_menu"))) }
    val header by content { css("h1") }
}

class NavigationMenuModule(scope: WebElement) : Module(scope) {
    val homeLink by content { css(".nav_welcome") }
    val aboutLink by content { css(".nav_about") }
    val contactsLink by content { css(".nav_contacts") }

    fun toHome(): HomePage {
        homeLink.click()
        return at(::HomePage)
    }

    fun toAbout(): AboutPage {
        aboutLink.click()
        return at(::AboutPage)
    }

    fun toContacts(): ContactsPage {
        contactsLink.click()
        return at(::ContactsPage)
    }
}
