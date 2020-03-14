package org.kebish.usage.multipage

import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.jupiter.api.Test
import org.kebish.core.kebConfig
import org.kebish.junit5.KebTest
import org.kebish.usage.test.util.HttpResourceFolderServerExtension
import org.kebish.usage.test.util.extendable.Extendable
import org.kebish.usage.test.util.extendable.ExtendableImpl
import org.openqa.selenium.firefox.FirefoxDriver


class Experimental_PossilbeStyles_MultiPageTest : KebTest(kebConfig {
    WebDriverManager.firefoxdriver().setup()
    this.driver = { FirefoxDriver() }
}), Extendable by ExtendableImpl() {

    @Suppress("unused")
    var serverExtension = register(HttpResourceFolderServerExtension(browser, "org/kebish/usage/multipage"))




    //RECOMMENDED style
    // + pure keb functions for working with Page
    @Test
    fun `navigation menu works style5_2`() {
        to(::HomePage) {
            menu.toContacts()
        }.via(ContactsPage::class) {
            menu.toAbout()
        }
    }




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


    @Test
    fun `navigation menu works style2_1`() {
        val homePage = to(::HomePage)
        val contactsPage = homePage.menu.toContacts()
        contactsPage.menu.toAbout()
    }



    // + pure kotlin default functions - no magic
    @Suppress("unused")
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

    // POSSIBLE IDEA mechanismus, který hlídá, že jsi na správné stránce.
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


}
