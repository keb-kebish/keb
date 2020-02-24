package org.kebish.core.module

import io.github.bonigarcia.wdm.WebDriverManager
import kotlinx.html.body
import kotlinx.html.checkBoxInput
import org.assertj.core.api.Assertions
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

class CheckboxTest: KebTest(Browser(kebConfig {
    WebDriverManager.firefoxdriver().setup()
    this.driver = { FirefoxDriver() }
})), Extendable by ExtendableImpl() {

    private val serverExtension = register(HttpBuilderServerExtension(
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
        Assertions.assertThat(checkbox.isChecked()).isTrue()

        // when
        checkbox.uncheck()

        // then
        Assertions.assertThat(checkbox.isChecked()).isFalse()
    }
}

class CheckboxTestPage : Page() {

    override fun at() = checkbox

    val checkbox by content { module(Checkbox(css(".checkbox"))) }
}