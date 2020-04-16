package org.kebish.core.browser.management

import kotlinx.html.ATarget
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.id
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.kebish.core.browser.provider.StaticBrowserProvider
import org.kebish.junit5.KebTest
import org.kebish.test.config.commonTestKebConfig
import org.kebish.usage.test.util.HtmlContent
import org.kebish.usage.test.util.HttpBuilderServerExtension
import org.kebish.usage.test.util.extendable.Extendable
import org.kebish.usage.test.util.extendable.ExtendableImpl

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ClosingWindowsAndTabsAfterEachTestBasicTest : KebTest(
    commonTestKebConfig().apply {
        browserProvider = StaticBrowserProvider(
            this,
            openNewEmptyWindowAndCloseOtherAfterEachTest = true // This is default, but this option is being tested here
        )
    }
), Extendable by ExtendableImpl() {

    @Suppress("unused")
    private val serverExtension = register(
        HttpBuilderServerExtension(
            browser,
            HtmlContent {
                body {
                    a("") {
                        id = "NewTabLink"
                        target = ATarget.blank
                        +"New tab link"
                    }
                }
            }
        ))


    @Test
    @Order(1)
    fun `'given', 'when' part of test`() {
        // Given
        browser.driver.get(browser.baseUrl)

        // When - test open multiple windows
        waitFor {
            css("#NewTabLink").click()
        }
        waitFor {
            css("#NewTabLink").click()
        }
        waitFor {
            css("#NewTabLink").click()
        }

    }

    @Test
    @Order(2)
    fun `'then' part of the test`() {
        // Then - all tabs from previous test were closed - only one is opened
        assertThat(browser.driver.windowHandles).hasSize(1)

        // And - opened window is empty
        browser.driver.pageSource.contains("<body></body>")
    }


}