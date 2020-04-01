package org.kebish.core.browser

import kotlinx.html.ATarget
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.id
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.kebish.junit5.KebTest
import org.kebish.test.config.commonTestKebConfig
import org.kebish.usage.test.util.HtmlContent
import org.kebish.usage.test.util.HttpBuilderServerExtension
import org.kebish.usage.test.util.extendable.Extendable
import org.kebish.usage.test.util.extendable.ExtendableImpl
import org.openqa.selenium.JavascriptExecutor

class CloseAllWindowsExceptOneAfterTest : KebTest(
    commonTestKebConfig(

    )
), Extendable by ExtendableImpl() {

    @Suppress("unused")
    private val serverExtension = register(HttpBuilderServerExtension(
        browser,
        HtmlContent {
            body {
                a("") {
                    id = "NewTabLink"
                    target = ATarget.blank
                    +"New tab link"
                }
            }
        }
    ))


    @Disabled("Not finished yet")
    @Test
    fun `quit browser in the middle of the test - create new driver`() {
        // Given
        browser.driver.get(browser.baseUrl)

        waitFor {
            css("#NewTabLink").click()
        }
        waitFor {
            css("#NewTabLink").click()
        }
        waitFor {
            css("#NewTabLink").click()
        }


        openNewTabAndCloseOtherTabsAndWindows()


    }

    private fun openNewTabAndCloseOtherTabsAndWindows() {

        val originalHandles = browser.driver.windowHandles
        val driver = browser.driver
        if (driver is JavascriptExecutor) {
            driver.executeScript("window.open();")
        }
        val newHandles = browser.driver.windowHandles

        val difference = newHandles.minus(originalHandles)


        if (difference.size != 1) {
            throw IllegalStateException("Diff - $difference, orig - $originalHandles, new - $newHandles ")
        }


        val newEmptyWindow = difference.first()
        val windowsToClose = newHandles.minus(newEmptyWindow)
        windowsToClose.forEach { closeTab(it) }

        driver.switchTo().window(newEmptyWindow)


//        val windowHandles = browser.driver.windowHandles.toList()
//        if (windowHandles.size > 1) {
//            browser.driver.switchTo().window(windowHandles.first())
//            for (handle in windowHandles.subList(1, windowHandles.size)) {
//                browser.driver.close()
//                browser.driver.switchTo().window(handle)
//            }
//        }
    }

    private fun closeTab(windowHandle: String) {
        browser.driver.switchTo().window(windowHandle)
        browser.driver.close() //TODO if window do not close, try close opened dialogs
        //        browser.driver.switchTo().alert().dismiss()
    }

    private fun closeAllWindowsAndTabsExceptOneLeftLast() {
        val windowHandles = browser.driver.windowHandles.toList()
        if (windowHandles.size > 1) {
            browser.driver.switchTo().window(windowHandles.first())
            for (handle in windowHandles.subList(1, windowHandles.size)) {
                browser.driver.close()
                browser.driver.switchTo().window(handle)
            }
        }
    }

    private fun closeAllWindowsAndTabsExceptOneFirst() {
        val windowHandles = browser.driver.windowHandles.toList()
        browser.driver.switchTo().window(windowHandles.last())
        for (handle in windowHandles.subList(0, windowHandles.size - 1).reversed()) {
            browser.driver.close()
            browser.driver.switchTo().window(handle)
        }
    }

//    val windowHandles = browser.driver.windowHandles.toList()
//    var l= mutableListOf<Int>()
//    for(i in windowHandles.size-1 downTo 0){
//
//       l.add(i)
//    }
//    l


//    val toList = browser.driver.windowHandles.toList()
//    browser.driver.switchTo().window(toList[3])


//
//    driver.execute_script('''window.open("https://some.site/", "_blank");''')
//    sleep(1) # you can also try without it, just playing safe
//    driver.switch_to_window(driver.window_handles[-1]) # last opened tab handle
//    # driver.switch_to.window(driver.window_handles[-1])  for newer versions.
}