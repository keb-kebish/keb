package org.kebish.core.browser

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.then
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kebish.core.config.kebConfig
import org.openqa.selenium.WebDriver

internal class BrowserSetUrlTest {

    @Test
    fun `simple set of full url`() {
        // given
        val driverMock = mock<WebDriver>()
        val browser = Browser(kebConfig { driver = { driverMock } })

        // when
        browser.url = "http://kebish.org/myTestUrl"

        // then
        val urlCaptor = argumentCaptor<String>()
        then(driverMock).should().get(urlCaptor.capture())
        assertThat(urlCaptor.firstValue).isEqualTo("http://kebish.org/myTestUrl")
    }

    @Test
    fun `simple get of url delegates to driver`() {
        // given
        val driverMock = mock<WebDriver>()
        given(driverMock.currentUrl).willReturn("https://kebish.org/testing/current-url")
        val browser = Browser(kebConfig { driver = { driverMock } })

        // when
        val url = browser.url

        // then
        assertThat(url).isEqualTo("https://kebish.org/testing/current-url")
    }


}