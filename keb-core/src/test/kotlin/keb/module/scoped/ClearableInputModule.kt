package keb.module.scoped

import com.horcacorp.testing.keb.core.Browser
import com.horcacorp.testing.keb.core.ScopedModule
import org.openqa.selenium.WebElement

class ClearableInputModule(browser: Browser, scope: WebElement) : ScopedModule(browser, scope) {

    val label = html("label")
    val textInput = css("""input[type="text"]""")
    val clearButton = css("""input[type="button"]""")


    var value: String
        get() = textInput.getAttribute("value")
        set(newValue) {
            clearButton.click()
            textInput.sendKeys(newValue)
        }


}