package org.kebish.usage.module.scoped

import org.kebish.core.Page

class PageWithModulesPage : Page() {
    override fun url() = "page.html"
    override fun at() = css("#name")


    val name by content { module(ClearableInputModule(css("#name"))) }
    val surname by content { module(ClearableInputModule(css("#surname"))) }

}