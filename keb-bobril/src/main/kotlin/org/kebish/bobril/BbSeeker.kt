package org.kebish.bobril

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

internal object BbSeeker {

    @Suppress("UNCHECKED_CAST")
    fun findElements(selector: String, driver: WebDriver, scope: WebElement? = null): List<WebElement> {
        val javascriptExecutor = driver.asJavascriptExecutor()
        val rootArg = scope?.let { ", arguments[0]" } ?: ""
        val searchScript = "return BBSeeker.findElements(\"$selector\"$rootArg);"
        return try {
            javascriptExecutor.executeScript(searchScript, listOf(scope)) as List<WebElement>?
        } catch (e: Exception) {
            injectBbSeeker(javascriptExecutor)
            javascriptExecutor.executeScript(searchScript, listOf(scope)) as List<WebElement>?
        } ?: emptyList()
    }

    private fun WebDriver.asJavascriptExecutor(): JavascriptExecutor = if (this is JavascriptExecutor) {
        this
    } else {
        throw RuntimeException(
            "Unable to select Bobril elements. WebDriver is not instanceof 'JavascriptExecutor', " +
                    "unable to inject BBSeeker script."
        )
    }

    private fun injectBbSeeker(javascriptExecutor: JavascriptExecutor) {
        val script = javaClass
            .classLoader
            .getResource("org/kebish/bobril/bbSeeker.js")
            ?.readText()
            ?: throw RuntimeException("Unable to select Bobril elements. Cannot read BBSeeker file.")
        javascriptExecutor.executeScript(script)
    }

}