package org.kebish.usage.navigation

import io.github.bonigarcia.wdm.WebDriverManager
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.kebish.core.Browser
import org.kebish.core.kebConfig
import org.kebish.junit5.KebTest
import org.kebish.usage.multipage.ContactsPage
import org.kebish.usage.test.util.HttpResourceFolderServerExtension
import org.kebish.usage.test.util.extendable.Extendable
import org.kebish.usage.test.util.extendable.ExtendableImpl
import org.openqa.selenium.firefox.FirefoxDriver

class NavigationTest : KebTest(Browser(kebConfig {
    WebDriverManager.firefoxdriver().setup()
    this.driver = { FirefoxDriver() }
})), Extendable by ExtendableImpl() {

    @Suppress("unused")
    var serverExtension = register(HttpResourceFolderServerExtension("org/kebish/usage/multipage", browser))

    @Test
    fun `to without closure returns page`() {
        // when
        val toResult = to(::ContactsPage)

        // then
        Assertions.assertThat(toResult).isExactlyInstanceOf(ContactsPage::class.java)

    }
    
}