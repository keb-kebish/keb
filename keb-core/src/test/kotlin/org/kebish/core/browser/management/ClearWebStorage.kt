package org.kebish.core.browser.management

import org.junit.jupiter.api.Test
import org.kebish.core.browser.provider.StaticBrowserProvider
import org.kebish.junit5.KebTest
import org.kebish.test.config.commonTestKebConfig

class ClearWebStorage : KebTest(commonTestKebConfig().apply {
    browserProvider = StaticBrowserProvider(
        clearWebStorageAfterEachTest = true
    )
}) {

    @Test
    fun `clearWebStorageAfterEachTest on empty browser`() {
        StaticBrowserProvider.reset()
        // When - open empty browser
        browser.driver
        // Then - `clearWebStorageAfterEachTest` - will work and test will not fail
    }

}