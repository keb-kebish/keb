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
        val enabledModule = page.enabledModule

        // then
        assertThat(enabledModule.isEnabled()).isTrue()
        assertThat(enabledModule.isDisabled()).isFalse()

        // when
        val disabledModule = page.disabledModule

        // then
        assertThat(disabledModule.isEnabled()).isFalse()
        assertThat(disabledModule.isDisabled()).isTrue()

        // when
        val editableModule = page.editableModule

        // then
        assertThat(editableModule.isEditable()).isTrue()
        assertThat(editableModule.isReadOnly()).isFalse()

        // when
        val readonlyModule = page.readonlyModule

        // then
        assertThat(readonlyModule.isEditable()).isFalse()
        assertThat(readonlyModule.isReadOnly()).isTrue()
    }
}

private class FormElementTestPage : Page() {
    val enabledModule by content { module(TextInput(css(".enabled"))) }
    val disabledModule by content { module(TextInput(css(".disabled"))) }
    val editableModule by content { module(TextInput(css(".editable"))) }
    val readonlyModule by content { module(TextInput(css(".readonly"))) }
}