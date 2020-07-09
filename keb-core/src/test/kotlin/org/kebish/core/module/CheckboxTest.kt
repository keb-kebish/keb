package org.kebish.core.module

import kotlinx.html.body
import kotlinx.html.checkBoxInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kebish.core.Page
import org.kebish.junit5.KebTest
import org.kebish.test.config.commonTestKebConfig
import org.kebish.usage.test.util.HtmlContent
import org.kebish.usage.test.util.HttpBuilderServerExtension
import org.kebish.usage.test.util.extendable.Extendable
import org.kebish.usage.test.util.extendable.ExtendableImpl

class CheckboxTest : KebTest(commonTestKebConfig())
    , Extendable by ExtendableImpl() {

    @Suppress("unused")
    private val serverExtension = register(
        HttpBuilderServerExtension(
            browser,
            HtmlContent {
                body {
                    checkBoxInput(classes = "checkbox") {
                        checked = false
                    }
                }
        }
    ))

    @Test
    fun `checkbox test`() {
        // given
        val checkbox = to(::CheckboxTestPage).checkbox

        // when
        checkbox.check()

        // then
        assertThat(checkbox.isChecked()).isTrue()

        // when
        checkbox.uncheck()

        // then
        assertThat(checkbox.isChecked()).isFalse()
    }
}

class CheckboxTestPage : Page() {

    override fun at() = checkbox

    val checkbox by content { module(Checkbox(css(".checkbox"))) }
}