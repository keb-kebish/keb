package org.kebish.usage

import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.kebish.core.*
import org.kebish.junit5.KebTest
import org.openqa.selenium.WebElement
import org.openqa.selenium.firefox.FirefoxDriver

class KotlinSiteKebTest : KebTest(Browser(kebConfig {
    WebDriverManager.firefoxdriver().setup()
    driver = { FirefoxDriver() }
    baseUrl = "https://kotlinlang.org"
})) {

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

class KotlinHomePage : Page() {
    override fun url() = "/"
    override fun at() = header

    val header by content { css(".global-header-logo") }
    val menu by content { module(NavMenuModule(css(".nav-links"))) }
    val footer by content { module(FooterModule(html("footer"))) }

    fun openDocumentation(): KotlinDocumentationPage {
        menu.menuItems.first { it.text.contains("learn", ignoreCase = true) }.click()
        return at(::KotlinDocumentationPage)
    }

    fun licensedUnderApacheLicense() = footer.licenseNotice.text.contains("apache", ignoreCase = true)

}

class KotlinDocumentationPage : Page() {
    override fun url() = "/docs/reference"
    override fun at() = title

    val title by content { html("h1") }

}

class NavMenuModule(scope: WebElement) : Module(scope) {
    val menuItems by content { htmlList("a") }
}

class FooterModule(scope: WebElement) : Module(scope) {
    val licenseNotice by content { css(".terms-copyright") }
    val sponsor by content { css(".terms-sponsor") }
}