package org.kebish.core.module

import io.github.bonigarcia.wdm.WebDriverManager
import kotlinx.html.body
import kotlinx.html.checkBoxInput
import kotlinx.html.textInput
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
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

class TextInputTest : KebTest(Browser(kebConfig {
    WebDriverManager.firefoxdriver().setup()
    this.driver = { FirefoxDriver() }
})), Extendable by ExtendableImpl() {

    private val serverExtension = register(HttpBuilderServerExtension(
        browser,
        HtmlContent {
            body {
                textInput(classes = "text") { value = "initial value" }
                checkBoxInput(classes = "checkbox")
            }
        }
    ))

    @Test
    fun `testing text input module`() {
        // given
        val inputModule = to(::TextInputTestPage).input

        // when
        val initialValue = inputModule.text

        // then
        assertThat(initialValue).isEqualTo("initial value")

        // when
        inputModule.text = "testing"

        // then
        assertThat(inputModule.text).isEqualTo("testing")
    }

    @Test
    fun `text input module cannot be used for non-text input module`() {
        // given
        val page = to(::TextInputTestPage)

        // when
        val exception = catchThrowable { page.checkbox }

        // then
        assertThat(exception)
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("only input of type 'text' is allowed")

    }

}

class TextInputTestPage : Page() {

    override fun at() = input

    val input by content { module(TextInput(css(".text"))) }
    val checkbox by content { module(TextInput(css(".checkbox"))) }
}