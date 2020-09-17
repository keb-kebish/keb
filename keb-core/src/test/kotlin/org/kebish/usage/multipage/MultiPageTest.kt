package org.kebish.usage.multipage

import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.jupiter.api.Test
import org.kebish.core.config.kebConfig
import org.kebish.core.module.Module
import org.kebish.core.page.Page
import org.kebish.junit5.KebTest
import org.kebish.usage.test.util.HttpResourceFolderServerExtension
import org.kebish.usage.test.util.extendable.Extendable
import org.kebish.usage.test.util.extendable.ExtendableImpl
import org.openqa.selenium.WebElement
import org.openqa.selenium.firefox.FirefoxDriver


class MultiPageTest : KebTest(kebConfig {
    WebDriverManager.firefoxdriver().setup()
    this.driver = { FirefoxDriver() }
}), Extendable by ExtendableImpl() {

    var serverExtension = register(HttpResourceFolderServerExtension(browser, "org/kebish/usage/multipage"))

    @Test
    fun `navigation menu works`() {
        to(::HomePage) {
            menu.toContacts()
        }.via(ContactsPage::class) {
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
