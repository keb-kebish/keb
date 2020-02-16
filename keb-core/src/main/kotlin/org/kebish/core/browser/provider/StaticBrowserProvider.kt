package org.kebish.core.browser.provider

import org.kebish.core.Browser
import org.kebish.core.Configuration

class StaticBrowserProvider(configuration: Configuration) : BrowserProvider {

    init {
        //TODO - every instance reset static field - it should work, but think about it
        config = configuration
    }

    companion object {

        private lateinit var config: Configuration

        private val browser by lazy {
            //TODO why cannot compile
//            if (!::config.isInitialized) {
//                throw IllegalStateException("config was not initialized")
//            }
            Browser(config)
        }


    }

    override fun provideBrowser(): Browser {
        return browser
    }

}