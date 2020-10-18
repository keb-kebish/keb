package org.kebish.core.module.form

import org.openqa.selenium.WebElement

public class Checkbox(scope: WebElement) : FormElement(scope) {

    override fun getInputType(): String = "checkbox"

    public fun isChecked(): Boolean = formElement.isSelected

    public fun check() {
        if (!isChecked()) formElement.click()
    }

    public fun uncheck() {
        if (isChecked()) formElement.click()
    }

}