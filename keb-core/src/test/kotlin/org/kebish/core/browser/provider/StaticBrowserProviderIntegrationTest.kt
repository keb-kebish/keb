package org.kebish.core.browser.provider

import com.nhaarman.mockito_kotlin.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.kebish.core.Browser
import org.kebish.core.kebConfig
import org.kebish.junit5.KebTest
import org.openqa.selenium.WebDriver

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(PER_CLASS)
class StaticBrowserProviderIntegrationTest : KebTest(kebConfig {
    driver = {
        numberOfCalls++
        val driver: WebDriver = mock()
        driver
    }
    browserManagement.apply {
        clearCookiesAfterEachTest = false
        clearWebStorageAfterEachTest = false
    }
}) {

    companion object {
        var numberOfCalls = 0
        lateinit var browserFromTheFirstTest: Browser
    }

    @BeforeAll
    @AfterAll
    fun prepare() {
       StaticBrowserProvider.reset()
    }

    @Test
    @Order(1)
    fun `first call to driver initialize driver`() {
        // given - driver were not initialized
        assertThat(numberOfCalls).isEqualTo(0)

        // when
        val browserReference = browser
        browserReference.driver

        // then - driver was initialized
        assertThat(numberOfCalls).isEqualTo(1)

        // save browser for the last test
        browserFromTheFirstTest = browserReference
    }

    @Test
    @Order(2)
    fun `second call to driver do not initialize driver again, because driver was initialized before`() {
        // given - driver was already initialized
        assertThat(numberOfCalls).isEqualTo(1)

        // when
        browser.driver

        // then - no new initialization is done
        assertThat(numberOfCalls).isEqualTo(1)
    }

    @Test
    @Order(3)
    fun `the same browser is used all the time`() {

        // when
        val obtainedBrowser = browser

        // then - no new initialization is done
        assertThat(obtainedBrowser === browserFromTheFirstTest).isTrue()
    }
}