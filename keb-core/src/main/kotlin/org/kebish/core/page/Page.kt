package org.kebish.core.page

import org.kebish.core.*
import org.kebish.core.browser.Browser

abstract class Page : ContentSupport, ModuleSupport, NavigationSupport, WaitSupport {

    override lateinit var browser: Browser

    open fun url(): String = ""
    open fun at(): Any =
        if (browser.config.atVerifierRequired) {
            throw AtVerifierNotDefined(this)
        } else {
            true
        }

    internal fun verifyAt(wait: Any) = apply {
        WaitPresetFactory().from(wait, browser.config)
            ?.let { waitFor(it, desc = "Verifying of ${javaClass.simpleName}", f = ::at) }
            ?: at()
    }
}

class AtVerifierNotDefined(page: Page) :
    RuntimeException(
        "Required 'at' verifier for page '${page.javaClass.simpleName}' is not defined. " +
                "Define page verifier or disable its requirement in the configuration."
    )