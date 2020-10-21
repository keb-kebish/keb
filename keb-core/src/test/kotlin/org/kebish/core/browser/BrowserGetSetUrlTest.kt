package org.kebish.core.browser

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.then
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kebish.core.config.kebConfig
import org.openqa.selenium.WebDriver

internal class BrowserGetSetUrlTest {

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
    fun `set basic relative url`() {
        // given
        val (driverMock, browser) =
            init(baseUrl = "http://kebish.org/")

        // when
        browser.url = "relative-url"

        // then
        val urlCaptor = argumentCaptor<String>()
        then(driverMock).should().get(urlCaptor.capture())
        assertThat(urlCaptor.firstValue).isEqualTo("http://kebish.org/relative-url")
    }

    @Test
    fun `base url without trailing slash`() {
        // given
        val (driverMock, browser) =
            init(baseUrl = "http://kebish.org")

        // when
        browser.url = "relative-url"

        // then
        val urlCaptor = argumentCaptor<String>()
        then(driverMock).should().get(urlCaptor.capture())
        assertThat(urlCaptor.firstValue).isEqualTo("http://kebish.org/relative-url")
    }

    @Test
    fun `set empty url returns base url`() {
        // given
        val (driverMock, browser) =
            init(baseUrl = "https://kebish.org")

        // when
        browser.url = ""

        // then
        val urlCaptor = argumentCaptor<String>()
        then(driverMock).should().get(urlCaptor.capture())
        assertThat(urlCaptor.firstValue).isEqualTo("https://kebish.org")
    }


    private fun init(baseUrl: String): Pair<WebDriver, Browser> {
        val driverMock = mock<WebDriver>()
        val browser = Browser(kebConfig {
            driver = { driverMock }
            this.baseUrl = baseUrl
        })
        return Pair(driverMock, browser)
    }

    //TODO NoBaseUrlDefinedException

}