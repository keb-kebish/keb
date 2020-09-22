package org.kebish.core.module.form

import org.openqa.selenium.WebElement

class Checkbox(scope: WebElement) : FormElement(scope) {

    override fun getInputType() = "checkbox"

    fun isChecked() = formElement.isSelected

    fun check() {
        if (!isChecked()) formElement.click()
    }

    fun uncheck() {
        if (isChecked()) formElement.click()
    }

}