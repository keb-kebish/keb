package org.kebish.core.browser.management

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.then
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kebish.core.browser.provider.NewBrowserForEachTestProvider
import org.kebish.core.kebConfig
import org.kebish.junit5.KebTest
import org.mockito.Mockito.RETURNS_DEEP_STUBS

class QuitBrowserInTheMiddleOfTheTest : KebTest(
    kebConfig {
        driver = { mock(defaultAnswer = RETURNS_DEEP_STUBS) }
        browserProvider = NewBrowserForEachTestProvider(this)
    }

) {

    @Test
    fun `quit browser in the middle of the test - create new driver`() {
        // Given
        val driver1 = browser.driver

        // When
        browser.quit()

        // Then - assert driver quit was called
        then(driver1).should().quit()

        // When - get browser after quit
        val driver2 = browser.driver

        // Then - new driver is returned
        assertThat(driver2).isNotSameAs(driver1)
    }

}