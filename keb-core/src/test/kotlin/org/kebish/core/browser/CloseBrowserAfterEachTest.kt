package org.kebish.core.browser

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.then
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.TestMethodOrder
import org.kebish.core.kebConfig
import org.kebish.junit5.KebTest
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
    lateinit var driver2: WebDriver

    @Test
    @Order(1)
    fun `each test has different driver part 1`() {
        // When - frist test create driver
        driver1 = browser.driver
    }

    @Test
    @Order(2)
    fun `each test has different driver part 2`() {
        // And when - second test create driver
        driver2 = browser.driver

        // Then - driver from first test is different from driver used in second test
        assertThat(driver2).isNotSameAs(driver1)
    }

    @Test
    @Order(3)
    fun `first browser was closed`() {
        // And then drivers from previous tests were closed
        then(driver1).should().quit()
        then(driver2).should().quit()
    }

}