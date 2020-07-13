package org.kebish.core.browser.management

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.kebish.core.browser.provider.StaticBrowserProvider
import org.kebish.junit5.KebTest
import org.kebish.test.config.commonTestKebConfig
import org.kebish.usage.test.util.HttpResourceFolderServerExtension
import org.kebish.usage.test.util.extendable.Extendable
import org.kebish.usage.test.util.extendable.ExtendableImpl

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ClosingWindowsAndTabsAfterEachTest_WindowsWithAlertTest : KebTest(
    commonTestKebConfig().apply {
        browserProvider = StaticBrowserProvider(
            openNewEmptyWindowAndCloseOtherAfterEachTest = true // This is default, but this option is being tested here
        )
    }
), Extendable by ExtendableImpl() {

    var serverExtension = register(
        HttpResourceFolderServerExtension(browser, "org/kebish/core/browser/management")
    )


    @Test
    @Order(1)
    fun `'given', 'when' part of test`() {
        // Given
        browser.driver.get(browser.baseUrl)
        waitFor { css("#AlertLink") }

        // When - test open multiple windows

        css("#AlertLink").click()
        css("#ConfirmLink").click()
        css("#PromptLink").click()

        waitFor { assertThat(browser.driver.windowHandles).hasSize(4) }
    }

    @Test
    @Order(2)
    fun `'then' part of the test`() {
        // Then - all tabs from previous test were closed - only one is opened
        assertThat(browser.driver.windowHandles).hasSize(1)

        // And - opened window is empty
        assertThat(browser.pageSource).contains("<body></body>")
    }


}