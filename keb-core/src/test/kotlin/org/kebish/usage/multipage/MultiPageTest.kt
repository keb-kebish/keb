package org.kebish.usage.multipage

import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.jupiter.api.Test
import org.kebish.core.*
import org.kebish.junit5.KebTest
import org.kebish.usage.test.util.HttpResourceFolderServerExtension
import org.kebish.usage.test.util.extendable.Extendable
import org.kebish.usage.test.util.extendable.ExtendableImpl
import org.openqa.selenium.WebElement
import org.openqa.selenium.firefox.FirefoxDriver


class MultiPageTest : KebTest(Browser(kebConfig {
    WebDriverManager.firefoxdriver().setup()
    this.driver = { FirefoxDriver() }
})), Extendable by ExtendableImpl() {

    var serverExtension = register(HttpResourceFolderServerExtension("org/kebish/usage/multipage", browser))


    // Page objects are simple, do not return other page
    // When you are writing test. You decide and write on which page you are
    //
    //  + pages are more simple
    //  + you can see what is part of closure
    //  - during writing tests - you must know all pages and decide on which page you are
    @Test
    fun `navigation menu works style1`() {
        to(::HomePage) {
            menu.contactsLink.click()
        }

        at(::ContactsPage) {
            menu.aboutLink.click()
        }

        at(::AboutPage)
    }

//    @Test
//    fun `navigation menu works style1`() {
//        to(::HomePage) { homePage ->
//            homePage.menu.contactsLink.click()
//        }
//
//        at(::ContactsPage) { contactsPage ->
//            contactsPage.menu.aboutLink.click()
//        }
//
//        at(::AboutPage)
//    }


    // + Simple and strait forward old school,
    // - Page can be used even when browser is on different page when you are already
    //     - page can be reused - and no verification if you are really on page is executed
    @Test
    fun `navigation menu works style2`() {
        val homePage = to(::HomePage)
        homePage.menu.contactsLink.click()

        val contactsPage = at(::ContactsPage)
        contactsPage.menu.aboutLink.click()

        at(::AboutPage)
    }

    // + pure kotlin default functions - no magic
    @Test
    fun `navigation menu works style3`() {
        to(::HomePage).run {
            menu.toContacts()
        }.run {
            menu.toAbout()
        }
    }


    @Test
    fun `navigation menu works style4`() {
        to(::HomePage) {
            menu.toContacts()
        }.run {
            menu.toAbout()
        }
    }

    // + pure keb functions for working with Page
    @Test
    fun `navigation menu works style5`() {
        to(::HomePage) {
            menu.toContacts()
        }.via {
            menu.toAbout()
        }
    }

    // + pure keb functions for working with Page
    @Test
    fun `navigation menu works style5_2`() {
        to(::HomePage) {
            menu.toContacts()
        }.via(ContactsPage::class) {
            menu.toAbout()
        }
    }


    //TODO mechanismus, který hlídá, že jsi na správné stránce.
    // neco jak, že by si browser hlídal na jaké jsi stránce (třeba při validátoru by si to zapamatoval)
    // A pak by hlídal, že se volá jen z instancí té strpávné stránky (problém je, že tam nevím, kde se na to chytit...
    // To bych musel se chytit někde někde před metodou, která je naimplementovaná na stránce a to je problém.
    //     to bych musel obalit celý page object nějakou svojí proxy...    Jako spring to už je potíž.
    //                                      Nesměl bych vytvářet stránky sám, ale jen přes keb ::MyPage
    // + pure keb functions for working with Page
//    Proč:  předcházet tomuhle
//    @Test
//    fun `navigation menu works style6`() {
//        to(::HomePage) {
//            menu.toContacts()
//        }.via {
//            menu.toAbout()  //Tady volám metodu z HomePage, ale veskutčnosti jsem na ContactsPage
//        }
//    }


    @Test
    fun `navigation menu works style7`() {
        to(::HomePage) {
            menu.toContacts()
        }.via {
            menu.toContacts()
        }.via(ContactsPage::class) {
            menu.toContacts()
        }.via2 { contactsPage ->
            contactsPage.menu.toContacts()
        }.via3 {
            menu.toContacts()
        }.via3 {
            it.menu.toContacts()
        }.via3 { contactsPage ->
            menu.toContacts()
        }.via3 { contactsPage ->
            contactsPage.menu.toContacts()
        }.via4(ContactsPage::class) {
            menu.toContacts()
        }


        //via3   is interesting, but it has so many possible variations, that there is too much freedom  :(

        //via4 is interesting - You cannot put there incorrect type and everytime you can see type of this
        //  maybe via4 could be new overriding method of via
        //  Then Both this could be possilbe:  and I would prefer second one
        //  .via {
        //      menu.toContacts()
        //  }
        //  .via(ContactsPage::class) {
        //      menu.toContacts()
        //  }

        at(::ContactsPage)
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
