package org.kebish.core.reporter

import kotlinx.html.body
import kotlinx.html.p
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.kebish.junit5.KebTest
import org.kebish.test.config.commonTestKebConfig
import org.kebish.usage.test.util.HtmlContent
import org.kebish.usage.test.util.HttpBuilderServerExtension
import org.kebish.usage.test.util.extendable.Extendable
import org.kebish.usage.test.util.extendable.ExtendableImpl
import java.io.File


@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class PageSourceReporterOfSuccessTest : KebTest(commonTestKebConfig().apply {
    reports.run {
        reporterDir = File("build/keb-reports-just-for-PageSourceReporterOfSuccessTest")
        // Given - success test is configured
        testSuccessReporters.add(PageSourceReporter())
    }
}), Extendable by ExtendableImpl() {


    @Suppress("unused")
    private val serverExtension = register(HttpBuilderServerExtension(
        browser,
        HtmlContent {
            body {
                p(classes = "common-text") {
                    text("Screenshot testing page")
                }
                p(classes = "common-text") {
                    text("Special characters - Žluťoučký kůn úpěl ďábelské ódy.")
                }
            }
        }
    ))

    @BeforeAll
    fun `clean reports directory`() {
        config.reports.reporterDir.deleteRecursively()
    }

    // When - test is successful
    @Test
    @Order(1)
    fun `successful test`() {
        browser.url = config.baseUrl
        waitFor { cssList(".common-text") }

    }

    // Then - screenshot was created
    @Test
    @Order(2)
    fun `report was created`() {
        val screenshots = config.reports.reporterDir.listFiles()!!
        assertThat(screenshots.size).isEqualTo(1)
        assertThat(screenshots[0].name).endsWith(".html")
        assertThat(screenshots[0].readText()).contains("Žluťoučký kůn úpěl ďábelské ódy.</p>")
    }

}