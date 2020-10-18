package org.kebish.core.module.form

import org.openqa.selenium.WebElement

public abstract class TextLikeInput(scope: WebElement) : FormElement(scope) {

    public var text: String
        get() = formElement.getAttribute("value")
        set(value) {
            formElement.clear()
            formElement.sendKeys(value)
        }
}