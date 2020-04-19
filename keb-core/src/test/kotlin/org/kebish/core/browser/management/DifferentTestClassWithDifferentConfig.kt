package org.kebish.core.browser.management

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kebish.junit5.KebTest
import org.kebish.test.config.commonTestKebConfig


/** Assert, that successing tests contains its own Configuration.
 * Important for situations, where you  */


class FirstConfigTest() : KebTest(commonTestKebConfig().apply {
    baseUrl = "First base url"
}) {
    @Test
    fun `test contains actual config`() {
        assertThat(config.baseUrl).isEqualTo("First base url")
    }

    @Test
    fun `browser contains actual config`() {
        assertThat(browser.config.baseUrl).isEqualTo("First base url")
    }
}

class SecondConfigTest() : KebTest(commonTestKebConfig().apply {
    baseUrl = "Second base url"
}) {
    @Test
    fun `test contains actual config`() {
        assertThat(config.baseUrl).isEqualTo("Second base url")
    }

    @Test
    fun `browser contains actual config`() {
        assertThat(browser.config.baseUrl).isEqualTo("Second base url")
    }
}