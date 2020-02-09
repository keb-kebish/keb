package org.kebish.core.module

import io.github.bonigarcia.wdm.WebDriverManager
import kotlinx.html.body
import kotlinx.html.textInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kebish.core.Browser
import org.kebish.core.Page
import org.kebish.core.kebConfig
import org.kebish.junit5.KebTest
import org.kebish.usage.test.util.HtmlContent
import org.kebish.usage.test.util.HttpBuilderServerExtension
import org.kebish.usage.test.util.extendable.Extendable
import org.kebish.usage.test.util.extendable.ExtendableImpl
import org.openqa.selenium.firefox.FirefoxDriver

class FormElementTest : KebTest(Browser(kebConfig {
    WebDriverManager.firefoxdriver().setup()
    this.driver = { FirefoxDriver() }
})), Extendable by ExtendableImpl() {

    private val serverExtension = register(HttpBuilderServerExtension(
        browser,
        HtmlContent {
            body {
                textInput(classes = "enabled")
                textInput(classes = "disabled") { disabled = true }
                textInput(classes = "editable")
                textInput(classes = "readonly") { readonly = true }
            }
        }
    ))

    @Test
    fun `form element testing`() {
        // given
        val page = to(::FormElementTestPage)

        // when
        val enabled = page.enabled

        // then
        assertThat(enabled.isEnabled()).isTrue()
        assertThat(enabled.isDisabled()).isFalse()

        // when
        val disabled = page.disabled

        // then
        assertThat(disabled.isEnabled()).isFalse()
        assertThat(disabled.isDisabled()).isTrue()

        // when
        val editable = page.editable

        // then
        assertThat(editable.isEditable()).isTrue()
        assertThat(editable.isReadOnly()).isFalse()

        // when
        val readonly = page.readonly

        // then
        assertThat(readonly.isEditable()).isFalse()
        assertThat(readonly.isReadOnly()).isTrue()
    }
}

private class FormElementTestPage : Page() {
    val enabled by content { module(TextInput(css(".enabled"))) }
    val disabled by content { module(TextInput(css(".disabled"))) }
    val editable by content { module(TextInput(css(".editable"))) }
    val readonly by content { module(TextInput(css(".readonly"))) }
}