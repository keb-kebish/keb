package org.kebish.core.browser.provider

import org.kebish.core.Browser
import org.kebish.core.Configuration

class StaticBrowserProvider {

    companion object {

        lateinit var config: Configuration

        private val browser by lazy {
            if (!::config.isInitialized) {
                throw IllegalStateException("config was not initialized")
            }
            Browser(config)
        }


    }

    fun provideBrowser(): Browser {
         return browser
    }

}