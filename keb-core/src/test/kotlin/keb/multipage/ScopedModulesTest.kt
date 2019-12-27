package keb.multipage

import com.horcacorp.testing.keb.core.Browser
import com.horcacorp.testing.keb.core.kebConfig
import io.github.bonigarcia.wdm.WebDriverManager
import keb.junit5.KebTest
import keb.test.util.ClasspathCopier
import keb.test.util.HttpFolderServer
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.openqa.selenium.firefox.FirefoxDriver
import java.nio.file.Path


class ScopedModulesTest : KebTest(Browser(kebConfig {
    WebDriverManager.firefoxdriver().setup()
    val driver = FirefoxDriver()
    this.driver = driver
    baseUrl =
        javaClass.classLoader.getResource("com/horcacorp/testing/keb/module/scoped/page.html")!!.toString()
            .replace("page.html", "")
})) {

    @Test
    fun `classpath copier works`(@TempDir tmpDir: Path) {
        ClasspathCopier.copyPackageIntoDirectory("keb.testing.multipages", tmpDir.toFile())
        Assertions.assertThat(tmpDir.toFile().list().toSet()).isEqualTo(setOf("index.html", "page1.html", "page2.html"))

        val server = HttpFolderServer(tmpDir).start()
        server.port

        browser.driver.get("http://localhost:8080/page1.html")
        readLine()
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



