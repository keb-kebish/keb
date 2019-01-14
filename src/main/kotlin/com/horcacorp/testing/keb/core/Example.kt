package com.horcacorp.testing.keb.core

import org.openqa.selenium.WebElement

class TestPage(browser: Browser) : Page(browser) {

    override fun url() = "https://kotlinlang.org"
    override fun at() = header

    val header by css(".global-header-logo", html("body"))
    val menu by scopedModule(::MenuModule, cssSelector(".nav-links"))

}

class MenuModule(browser: Browser, webElement: WebElement): ScopedModule(browser, webElement) {
    val links by htmlList("a")
}

fun main() {
    Browser.drive {
        val testPage = to(::TestPage)
        assert(testPage.header.text == "Kotlin")
        assert(testPage.menu.links.size == 3)
    }
}