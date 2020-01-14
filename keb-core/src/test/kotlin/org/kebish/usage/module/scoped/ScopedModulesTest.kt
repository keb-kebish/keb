package org.kebish.usage.module.scoped

import org.kebish.core.Browser
import org.kebish.core.kebConfig
import io.github.bonigarcia.wdm.WebDriverManager
import org.kebish.junit5.KebTest
import org.kebish.usage.test.util.HttpResourceFolderServerExtension
import org.kebish.usage.test.util.extendable.Extendable
import org.kebish.usage.test.util.extendable.ExtendableImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.openqa.selenium.firefox.FirefoxDriver


class ScopedModulesTest : KebTest(Browser(kebConfig {
    WebDriverManager.firefoxdriver().setup()
    this.driver = { FirefoxDriver() }
})), Extendable by ExtendableImpl() {

    var server = register(HttpResourceFolderServerExtension("org/kebish/usage/module/scoped", browser))

    @Test
    fun `surname can be cleared`() {
        // given
        val pageWithModulesPage = to(::PageWithModulesPage)
        assertThat(pageWithModulesPage.surname.value).isEqualTo("Doe")

        //when
        pageWithModulesPage.surname.clearButton.click()

        //then
        assertThat(pageWithModulesPage.surname.value).isEmpty()
    }

    @Test
    fun `surname can be cleared with page closure`() {
        // given
        to(::PageWithModulesPage) {
            assertThat(surname.value).isEqualTo("Doe")

            //when
            surname.clearButton.click()

            //then
            assertThat(surname.value).isEmpty()

        }
    }

    @Test
    fun `example of keb at function`() {
        // given
        to(::PageWithModulesPage)

        at(::PageWithModulesPage) {
            assertThat(surname.value).isEqualTo("Doe")

            //when
            surname.value = "My new Surname"

            //then
            assertThat(surname.value).isEqualTo("My new Surname")

        }
    }

}



