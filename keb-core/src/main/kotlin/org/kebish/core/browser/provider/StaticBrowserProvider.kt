package org.kebish.core.browser.provider

import org.kebish.core.Browser
import org.kebish.core.Configuration
import org.kebish.core.util.ResettableLazy

class StaticBrowserProvider(configuration: Configuration) : BrowserProvider {

    init {
        //TODO - every instance reset static field - it should work, but think about it
        config = configuration
    }

    companion object {

        private lateinit var config: Configuration

        private val browserDelegate = ResettableLazy(
            onReset = { browser -> browser.quit() }
        ) {
            val browser = Browser(config)
            //TODO there is no need to have extra thread for each browser
            //But there is no issue in StaticBrowser provider, because there is only one all the time
            //With multiple browsers consider - static WeakReference Set of browsers and only one shutdown hook
            Runtime.getRuntime().addShutdownHook(object : Thread() {
                override fun run() {
                    browser.quit()
                }
            })
            browser
        }

        private val browser by browserDelegate

        /**
         * Close browser and create new one on next call of provideDriver() method.
         */
        fun reset() {
            browserDelegate.reset()
        }

    }

    override fun provideBrowser(): Browser {
        return browser
    }


}