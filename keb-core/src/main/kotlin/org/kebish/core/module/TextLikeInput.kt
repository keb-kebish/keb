package org.kebish.core.module

import org.openqa.selenium.WebElement

abstract class TextLikeInput(scope: WebElement) : FormElement(scope) {

    var text: String
        get() = formElement.getAttribute("value")
        set(value) {
            formElement.clear()
            formElement.sendKeys(value)
        }
}