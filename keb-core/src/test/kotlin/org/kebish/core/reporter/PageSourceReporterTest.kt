package org.kebish.core.reporter

import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import org.junit.jupiter.api.Test
import org.kebish.core.Browser
import org.kebish.core.config.TestInfo

class PageSourceReporterTest {

    @Test
    // This is important for tests, which do not start browser. To prevent unintentional start of browser by PageSourceReporter
    fun `do not start new browser if browser is not initialized`() {
        // given
        val testInfo = mock<TestInfo>()
        val browser = mock<Browser>()
        given(browser.isDriverInitialized()).willReturn(false)

        // when
        PageSourceReporter().report(testInfo, browser)

        // then
        verify(browser).isDriverInitialized()
        verifyNoMoreInteractions(browser)


    }

}