package org.kebish.core.module

import org.kebish.core.Module
import org.openqa.selenium.WebElement

abstract class FormElement(scope: WebElement?) : Module(scope) {

    protected val formElement: WebElement = scope ?: throw IllegalArgumentException("No root for form element.")

    private val SUPPORTED_TAGS = listOf("button", "input", "option", "select", "textarea")

    init {
        checkTag()
        checkType()
    }

    abstract fun getInputType(): String

    fun isDisabled() = hasAttribute("disabled")

    fun isEnabled() = !isDisabled()

    fun isReadOnly() = hasAttribute("readonly")

    fun isEditable() = !isReadOnly()

    private fun checkTag() {
        val tag = formElement.tagName
        if (!SUPPORTED_TAGS.contains(tag.toLowerCase())) {
            throw IllegalArgumentException(
                "Specified root element for ${formElement.javaClass.name} module was '$tag', " +
                        "but only the following are allowed: [${SUPPORTED_TAGS.joinToString(", ")}]."
            )
        }
    }

    private fun checkType() {
        val type = formElement.getAttribute("type").toLowerCase()
        val allowedType = getInputType()
        if (type != allowedType) {
            throw IllegalArgumentException(
                "Specified root element for ${formElement.javaClass.name} module was an input of type '$type', " +
                        "but only input of type '$allowedType' is allowed as the base root element."
            )
        }
    }

    private fun hasAttribute(attribute: String): Boolean {
        val value = formElement.getAttribute(attribute)
        return value == attribute || value == "true"
    }

}