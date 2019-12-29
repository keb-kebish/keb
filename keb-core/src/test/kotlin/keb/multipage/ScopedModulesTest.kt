package keb.multipage

import com.horcacorp.testing.keb.core.Browser
import com.horcacorp.testing.keb.core.kebConfig
import io.github.bonigarcia.wdm.WebDriverManager
import keb.junit5.KebTest
import keb.test.util.HttpResourceFolderServer
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
    fun `resource dir server works`() {
        val server = HttpResourceFolderServer("keb/testing/multipage")
        println("PORT:" + server.port)

        browser.driver.get("http://localhost:${server.port}/")
    }


//    @Test
//    fun `example of keb at function`(@TempDir tmpDir :Path) {
//        // given
//        to(::PageWithModulesPage)   //TODO better example would be have to have html with two pages...
//
//        at(::PageWithModulesPage) {
//            assertThat(surname.value).isEqualTo("Doe")
//
//            //when
//            surname.value = "My new Surname"
//
//            //then
//            assertThat(surname.value).isEqualTo("My new Surname")
//
//        }
//    }

}



