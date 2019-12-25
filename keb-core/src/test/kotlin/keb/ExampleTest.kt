package keb

import com.horcacorp.testing.keb.core.Browser
import com.horcacorp.testing.keb.core.Page
import com.horcacorp.testing.keb.core.ScopedModule
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.openqa.selenium.WebElement

class KotlinSiteKebTest : KebTest() {

    @Test
    fun `testing kotlin lang page`() {
        // given
        val homePage = to(::KotlinHomePage)

        // when
        val title = homePage.header

        // then
        Assertions.assertEquals("Kotlin", title.text)
        Assertions.assertTrue(homePage.licensedUnderApacheLicense())

        // when
        val docsPage = homePage.openDocumentation()

        // then
        Assertions.assertEquals("Learn Kotlin", docsPage.title.text)
    }

}

class KotlinHomePage(browser: Browser) : Page(browser) {
    override fun url() = "/"
    override fun at() = header

    val header = css(".global-header-logo")
    val menu = scopedModule(::NavMenuModule, css(".nav-links"))
    val footer = scopedModule(::FooterModule, html("footer"))

    fun openDocumentation(): KotlinDocumentationPage {
        menu.menuItems.first { it.text == "LEARN" }.click()
        return at(::KotlinDocumentationPage)
    }

    fun licensedUnderApacheLicense() = footer.licenseNotice.text.contains("apache", ignoreCase = true)

}

class KotlinDocumentationPage(browser: Browser) : Page(browser) {
    override fun url() = "/docs/reference"
    override fun at() = title

    val title = html("h1")

}

class NavMenuModule(browser: Browser, scope: WebElement) : ScopedModule(browser, scope) {
    val menuItems = htmlList("a")
}

class FooterModule(browser: Browser, scope: WebElement) : ScopedModule(browser, scope) {
    val licenseNotice = css(".terms-copyright")
    val sponsor = css(".terms-sponsor")
}