package org.kebish.core.reporter

import kotlinx.html.body
import kotlinx.html.p
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.kebish.junit5.KebTest
import org.kebish.test.config.commonTestKebConfig
import org.kebish.usage.test.util.HtmlContent
import org.kebish.usage.test.util.HttpBuilderServerExtension
import org.kebish.usage.test.util.extendable.Extendable
import org.kebish.usage.test.util.extendable.ExtendableImpl

class ScreenshotReporterOfFailedTest : KebTest(commonTestKebConfig().apply {
    reports.testFailReporters.add(ScreenshotReporter())
}), Extendable by ExtendableImpl() {

    @Suppress("unused")
    private val serverExtension = register(HttpBuilderServerExtension(
        browser,
        HtmlContent {
            body {
                p(classes = "common-text") {
                    text("Screenshot testing page")
                }
            }
        }
    ))

    @Disabled("Works, but reports but fails - it needs to be tested in other way")
    @Test
    fun `checkbox test`() {
        browser.url = config.baseUrl
        waitFor { css(".common-text") }


        //TODO make it work somehow
        throw(Exception("AAAAAAAAA  FAILING TEST!!!"))
//        println("TEST SUCCESSS")
    }
    //TODO check, that screenshot was created

}
