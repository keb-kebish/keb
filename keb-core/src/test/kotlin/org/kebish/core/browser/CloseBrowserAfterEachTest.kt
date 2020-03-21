package org.kebish.core.browser

import com.nhaarman.mockito_kotlin.mock
import org.junit.jupiter.api.Test
import org.kebish.core.browser.provider.StaticBrowserProviderIntegrationTest
import org.kebish.core.kebConfig
import org.kebish.junit5.KebTest
import org.kebish.test.config.commonTestKebConfig
import org.openqa.selenium.WebDriver

class CloseBrowserAfterEachTest : KebTest(commonTestKebConfig().apply {
    //TODO set close browser after each test
    browserManagement.apply {
//        closeBrowserAfterEachTest = true
    }
}) {

     //TODO finish this test

    @Test
    fun `each test has different browser 1`() {
//        val browser1 = browser

    }

    @Test
    fun `each test has different browser 2`() {

    }

}