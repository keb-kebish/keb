package org.kebish.core.browser

import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kebish.core.config.kebConfig
import org.openqa.selenium.WebDriver

internal class BrowserTest {

    @Test
    fun `pageSource return NO_PAGE_SOURCE_SUBSTITUTE in case driver_pageSource is null`() {
        // given
        val driverMock = mock<WebDriver>()
        given(driverMock.pageSource).willReturn(null)
        val browser = Browser(kebConfig { driver = { driverMock } })

        // when
        val pageSourceResult = browser.pageSource

        // then expected string is returned
        assertThat(pageSourceResult).isEqualTo("-- no page source --")

        // and is exactly expected constant
        assertThat(pageSourceResult).isSameAs(Browser.NO_PAGE_SOURCE_SUBSTITUTE)
    }
}