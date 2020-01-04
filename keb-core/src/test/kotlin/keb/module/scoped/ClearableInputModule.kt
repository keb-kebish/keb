package keb.module.scoped

import com.horcacorp.testing.keb.core.Module
import com.horcacorp.testing.keb.core.content
import com.horcacorp.testing.keb.core.css
import com.horcacorp.testing.keb.core.html
import org.openqa.selenium.WebElement

class ClearableInputModule(scope: WebElement) : Module(scope) {

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