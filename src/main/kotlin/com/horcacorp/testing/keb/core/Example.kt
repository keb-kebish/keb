package com.horcacorp.testing.keb.core

class TestPage(browser: Browser) : Page(browser) {

    override fun url() = "file:///Users/horca/development/index.html"
    override fun at() = header

    val header by html("h1")

}

fun main() {
    Browser.drive {
        val testPage = to(::TestPage)
        testPage.header.byCss("")
        if (testPage.header.text != "Hello vol.1") throw RuntimeException()
    }
}