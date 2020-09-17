package org.kebish.core.module.form

import kotlinx.html.body
import kotlinx.html.textInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kebish.core.page.Page
import org.kebish.junit5.KebTest
import org.kebish.test.config.commonTestKebConfig
import org.kebish.usage.test.util.HtmlContent
import org.kebish.usage.test.util.HttpBuilderServerExtension
import org.kebish.usage.test.util.extendable.Extendable
import org.kebish.usage.test.util.extendable.ExtendableImpl

class FormElementTest : KebTest(commonTestKebConfig()),
    Extendable by ExtendableImpl() {

    @Suppress("unused")
    private val serverExtension = register(
        HttpBuilderServerExtension(
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