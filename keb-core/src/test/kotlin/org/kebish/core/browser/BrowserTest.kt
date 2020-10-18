package org.kebish.core.browser

import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kebish.core.config.kebConfig
import org.mockito.Mockito.RETURNS_DEEP_STUBS
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

    @Test
    fun `title will delegate to driver title`() {
        // given
        val driverMock = mock<WebDriver>()
        given(driverMock.title).willReturn("Mocked Title")
        val browser = Browser(kebConfig { driver = { driverMock } })

        // when
        val title = browser.title

        // then
        assertThat(title).isEqualTo("Mocked Title")
        verify(driverMock).title
    }

    @Test
    fun `refresh will delegate to driver refresh`() {
        // given
        val driverMock = mock<WebDriver>(defaultAnswer = RETURNS_DEEP_STUBS)
        val browser = Browser(kebConfig { driver = { driverMock } })

        // when
        browser.refresh()

        // then
        verify(driverMock.navigate()).refresh()
    }
}