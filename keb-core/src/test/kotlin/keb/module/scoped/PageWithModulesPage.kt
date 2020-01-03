package keb.module.scoped

import com.horcacorp.testing.keb.core.Browser
import com.horcacorp.testing.keb.core.Page
import com.horcacorp.testing.keb.core.content

class PageWithModulesPage(browser: Browser) : Page(browser) {
    override fun url() = "page.html"
    override fun at() = css("#name")


    val name by content { ClearableInputModule(browser, css("#name")) }
    val surname by content { ClearableInputModule(browser, css("#surname")) }

}