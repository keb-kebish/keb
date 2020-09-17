package org.kebish.core.browser.management

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.then
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.kebish.core.browser.provider.StaticBrowserProvider
import org.kebish.core.config.kebConfig
import org.kebish.junit5.KebTest
import org.mockito.Mockito.RETURNS_DEEP_STUBS
import org.mockito.Mockito.never
import org.openqa.selenium.WebDriver


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class SameBrowserIsUsedByEachTest : KebTest(
    kebConfig {
        driver = { mock(defaultAnswer = RETURNS_DEEP_STUBS) }
        //Static browser provider is default option
    }
) {
    lateinit var driver1: WebDriver
    lateinit var driver2: WebDriver

    @BeforeAll
    @AfterAll
    fun resetBrowser() {
        // BeforeAll - To "mock driver" and prevent using driver from previous test
        // AfterAll - To prevent passing "mock driver" to subsequent tests
        StaticBrowserProvider.reset()
    }

    @Test
    @Order(1)
    fun `each test has same driver by default part 1`() {
        // When - frist test create driver
        driver1 = browser.driver
    }

    @Test
    @Order(2)
    fun `each test has same driver by default part 2`() {
        // And when - second test create driver
        driver2 = browser.driver

        // Then - driver from first test is different from driver used in second test
        assertThat(driver2).isSameAs(driver1)
    }

    @Test
    @Order(3)
    fun `same driver is used - no quit were called yet`() {
        // And then drivers from previous tests were not closed yet
        then(driver1).should(never()).quit()

    }

}