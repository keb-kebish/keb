package org.kebish.core.browser.provider

import com.nhaarman.mockito_kotlin.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kebish.core.kebConfig
import org.mockito.BDDMockito.then
import org.openqa.selenium.WebDriver

class StaticBrowserProviderTest {

    @BeforeEach
    @AfterEach
    fun prepare() {
       StaticBrowserProvider.reset()
    }

    @Test
    fun `browser handling`() {
        // given
        StaticBrowserProvider.reset()
        val sbp = StaticBrowserProvider(kebConfig { })

        // when
        val browser1 = sbp.provideBrowser()

        // then
        assertThat(browser1).isNotNull

        // when
        val browser2 = sbp.provideBrowser()

        // then - next returned browser is exactly same instance
        assertThat(browser2 === browser1).isTrue()

        // when
        StaticBrowserProvider.reset()
        val browser3 = sbp.provideBrowser()

        // then new browser instance is created
        assertThat(browser3 === browser2).isFalse()
    }

    @Test
    fun `basic Test`() {
        // given
        StaticBrowserProvider.reset()
        var driverCreationCount = 0
        val driverMock : WebDriver = mock()
        val sbp = StaticBrowserProvider(kebConfig {
            driver = {
                driverCreationCount++
                driverMock
            }
        })
        assertThat(driverCreationCount).isEqualTo(0)

        // when
        sbp.provideBrowser().driver

        // then
        assertThat(driverCreationCount).isEqualTo(1)

        // when
        sbp.provideBrowser().driver

        // then -
        assertThat(driverCreationCount).isEqualTo(1)

        // when
        StaticBrowserProvider.reset()
        
        // then
        then(driverMock).should().quit()

        // when
        sbp.provideBrowser().driver

        // then
        assertThat(driverCreationCount).isEqualTo(2)
    }


}