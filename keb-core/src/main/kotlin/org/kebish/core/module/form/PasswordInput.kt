package org.kebish.core.module.form

import org.openqa.selenium.WebElement

public class PasswordInput(scope: WebElement) : TextLikeInput(scope) {

    override fun getInputType(): String = "password"
}