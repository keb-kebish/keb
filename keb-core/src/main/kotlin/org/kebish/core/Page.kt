package org.kebish.core

abstract class Page : ContentSupport, ModuleSupport, NavigationSupport, WaitSupport {

    override lateinit var browser: Browser

    open fun url(): String = ""
    open fun at(): Any = if (browser.config.atVerifierRequired) {
        throw AtVerifierNotDefined(this)
    } else {
        true
    }

    fun verifyAt(waitPreset: String?) {
        waitFor(
            waitPreset, desc = "Verifying of ${javaClass.simpleName}", f = ::at
        )
    }
}

class AtVerifierNotDefined(page: Page) :
    RuntimeException("Required 'at' verifier for page '${page.javaClass.simpleName}' is not defined. " +
            "Define page verifier or disable its requirement in the configuration.")