package keb.module.scoped

import com.horcacorp.testing.keb.core.Browser
import com.horcacorp.testing.keb.core.ScopedModule
import com.horcacorp.testing.keb.core.content
import org.openqa.selenium.WebElement

class ClearableInputModule(browser: Browser, scope: WebElement) : ScopedModule(browser, scope) {

    val label by content { html("label") }
    val textInput by content { css("""input[type="text"]""") }
    val text by content { textInput.getAttribute("value") }
    val clearButton by content { css("""input[type="button"]""") }


    var value: String
        get() = text
        set(newValue) {
            clearButton.click()
            textInput.sendKeys(newValue)
        }


}