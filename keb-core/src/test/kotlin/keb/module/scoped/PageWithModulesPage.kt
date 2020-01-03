package keb.module.scoped

import com.horcacorp.testing.keb.core.Page
import com.horcacorp.testing.keb.core.content
import com.horcacorp.testing.keb.core.css

class PageWithModulesPage : Page() {
    override fun url() = "page.html"
    override fun at() = css("#name")


    val name by content { module(ClearableInputModule(css("#name"))) }
    val surname by content { module(ClearableInputModule(css("#surname"))) }

}