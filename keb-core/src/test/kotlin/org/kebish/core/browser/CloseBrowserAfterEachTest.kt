package org.kebish.core.browser

import com.nhaarman.mockito_kotlin.mock
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.kebish.core.Browser
import org.kebish.core.browser.provider.StaticBrowserProviderIntegrationTest
import org.kebish.core.kebConfig
import org.kebish.junit5.KebTest
import org.kebish.test.config.commonTestKebConfig
import org.openqa.selenium.WebDriver

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation::class)
class CloseBrowserAfterEachTest : KebTest(kebConfig {
    driver = { mock() }
    browserManagement.apply {
        closeBrowserAfterEachTest = true
    }
}) {

    lateinit var driver1: WebDriver

    @Test
    @Order(1)
    fun `each test has different browser 1`() {
        driver1 = browser.driver

    }

    @Test
    @Order(2)
    fun `each test has different browser 2`() {
        val driver2 = browser.driver

        //Then - driver from first test is different from driver used in second test
        assertThat(driver2).isNotSameAs(driver1)
    }

}