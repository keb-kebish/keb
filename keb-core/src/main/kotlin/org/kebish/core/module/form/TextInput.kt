package org.kebish.core.module.form

import org.openqa.selenium.WebElement

public class TextInput(scope: WebElement) : TextLikeInput(scope) {

    override fun getInputType(): String = "text"

}