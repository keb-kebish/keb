package org.kebish.core.browser.management

import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.then
import org.junit.jupiter.api.*
import org.kebish.core.browser.provider.StaticBrowserProvider
import org.kebish.junit5.KebTest
import org.kebish.test.config.commonTestKebConfig
import org.kebish.usage.test.util.extendable.Extendable
import org.kebish.usage.test.util.extendable.ExtendableImpl
import org.mockito.Mockito.RETURNS_DEEP_STUBS
import org.mockito.Mockito.times
import org.openqa.selenium.WebDriver

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ClosingWindowsAndTabsAfterEachTest_For_nonJavascriptExecutorDriverTest : KebTest(
    commonTestKebConfig().apply {
        browserProvider = StaticBrowserProvider(
            openNewEmptyWindowAndCloseOtherAfterEachTest = true // This is default, but this option is being tested here
        )
    }
), Extendable by ExtendableImpl() {

    val driver = mock<WebDriver>(defaultAnswer = RETURNS_DEEP_STUBS)

    @BeforeAll
    fun setupMockDriver() {
        // Given driver is not type of JavascriptExecutorDriver
        config.driver = { driver }
        StaticBrowserProvider.reset()

        // And - driver simulate 3 open windows
        given(driver.windowHandles).willReturn(linkedSetOf("1", "2", "3"))
    }

    @AfterAll
    fun removeMockedDriver() {
        StaticBrowserProvider.reset()
    }

    @Test
    @Order(1)
    fun `'when' part of test`() {
        // when - driver is initialized
        browser.driver

    }

    @Test
    @Order(2)
    fun `'then' part of the test`() {
        //Then - two windows
        then(driver).should(times(2)).close()
    }


}