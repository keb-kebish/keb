package keb.module.scoped

import com.horcacorp.testing.keb.core.Browser
import com.horcacorp.testing.keb.core.kebConfig
import io.github.bonigarcia.wdm.WebDriverManager
import keb.junit5.KebTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.openqa.selenium.firefox.FirefoxDriver


class ScopedModulesTest : KebTest(Browser(kebConfig {
    WebDriverManager.firefoxdriver().setup()
    val driver = FirefoxDriver()
    this.driver = driver
    baseUrl =
        javaClass.classLoader.getResource("com/horcacorp/testing/keb/module/scoped/page.html")!!.toString()
            .replace("page.html", "")
})) {

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



