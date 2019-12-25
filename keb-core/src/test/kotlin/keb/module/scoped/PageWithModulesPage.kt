package keb.module.scoped

import com.horcacorp.testing.keb.core.Browser
import com.horcacorp.testing.keb.core.Page

class PageWithModulesPage(browser: Browser) : Page(browser) {
    override fun url() = "page.html"
    override fun at() = css("#name")


    val name = scopedModule(::ClearableInputModule, css("#name"))
    val surname = scopedModule(::ClearableInputModule, css("#surname"))

}