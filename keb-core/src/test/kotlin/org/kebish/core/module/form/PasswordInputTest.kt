package org.kebish.core.module.form

import kotlinx.html.body
import kotlinx.html.passwordInput
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.kebish.core.page.Page
import org.kebish.junit5.KebTest
import org.kebish.test.config.commonTestKebConfig
import org.kebish.usage.test.util.HtmlContent
import org.kebish.usage.test.util.HttpBuilderServerExtension
import org.kebish.usage.test.util.extendable.Extendable
import org.kebish.usage.test.util.extendable.ExtendableImpl

internal class PasswordInputTest : KebTest(commonTestKebConfig())
    , Extendable by ExtendableImpl() {

    @Suppress("unused")
    private val serverExtension = register(HttpBuilderServerExtension(
        browser,
        HtmlContent {
            body {
                passwordInput(classes = "pass") { value = "initial value" }
            }
        }
    ))

    @Test
    fun `testing text input module`() {
        // given
        val inputModule = to(::PasswordInputTestPage).input

        // when
        val initialValue = inputModule.text

        // then
        Assertions.assertThat(initialValue).isEqualTo("initial value")

        // when
        inputModule.text = "testing"

        // then
        Assertions.assertThat(inputModule.text).isEqualTo("testing")
    }

}

class PasswordInputTestPage : Page() {

    override fun at() = input

    val input by content { module(PasswordInput(css(".pass"))) }
}