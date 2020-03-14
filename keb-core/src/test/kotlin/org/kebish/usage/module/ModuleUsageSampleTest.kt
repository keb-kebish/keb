package org.kebish.usage.module

import kotlinx.html.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kebish.core.Module
import org.kebish.core.Page
import org.kebish.core.module.TextInput
import org.kebish.junit5.KebTest
import org.kebish.test.config.commonTestKebConfig
import org.kebish.usage.test.util.HtmlContent
import org.kebish.usage.test.util.HttpBuilderServerExtension
import org.kebish.usage.test.util.extendable.Extendable
import org.kebish.usage.test.util.extendable.ExtendableImpl
import org.openqa.selenium.WebElement

/** This sample show simple example, how you can define ClearableInputModule */
class ModuleUsageSampleTest : KebTest(commonTestKebConfig())
    , Extendable by ExtendableImpl() {

    @Suppress("unused")
    val serverExtension = register(
        HttpBuilderServerExtension(
            browser,
            page()
        )
    )

    fun page(): HtmlContent {
        return HtmlContent {
            body {
                h1 {
                    id = "ModulesPage"
                    text("Page with modules")
                }
                clearableInputModule("name", "Name", "John")
                clearableInputModule("surname", "Surname", "Doe")
            }
        }
    }

    fun BODY.clearableInputModule(moduleId: String, label: String, inputValue: String) {
        div {
            id = moduleId
            label {
                text("$label:")
            }
            input {
                type = InputType.text
                value = inputValue
            }
            input {
                type = InputType.button
                value = "Clear"
                onClick = "document.getElementById('$moduleId').childNodes[1].value = ''"
            }
        }
    }

    @Test
    fun `name can be cleared`() {
        // given
        to(::ModulesPage) {

            // when
            name.clearButton.click()

            // then
            assertThat(name.textInput.text).isEmpty()
        }
    }

    @Test
    fun `name can be cleared oldschool`() {
        // given
        val modulesPage = to(::ModulesPage)

        // when
        modulesPage.name.clearButton.click()

        // then
        assertThat(modulesPage.name.textInput.text).isEmpty()
    }

    class ModulesPage : Page() {
        override fun url() = ""

        override fun at() = header

        val header by content { css("#ModulesPage") }

        val name by content { module(ClearableInput(css("#name"))) }

        val surname by content { module(ClearableInput(css("#surname"))) }
    }

    class ClearableInput(scope: WebElement) : Module(scope) {
        val label by content { css("label").text }

        // notice, that on the page are two elements "input[@type='text']" (two modules)
        // because this selector look for elements only in defined "scope" you can use this simple selector
        val textInput by content { module(TextInput(xpath("input[@type='text']"))) }
        val clearButton by content { xpath("input[@type='button']") }

        // of course you can add methods into module
        fun clear() = clearButton.click()
    }


}