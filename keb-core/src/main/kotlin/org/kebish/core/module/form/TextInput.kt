package org.kebish.core.module.form

import org.openqa.selenium.WebElement

class TextInput(scope: WebElement) : TextLikeInput(scope) {

    override fun getInputType() = "text"

}