package org.kebish.core.module

import org.openqa.selenium.WebElement

class PasswordInput(scope: WebElement) : TextLikeInput(scope) {

    override fun getInputType() = "password"
}