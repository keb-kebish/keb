@file:Suppress("PropertyName", "MayBeConstant", "MapGetWithNotNullAssertionOperator")

package org.kebish.core

import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver

fun kebConfig(conf: Configuration.() -> Unit) = Configuration().apply { conf() }

class Configuration {

    companion object {
        val DEFAULT_PRESET_NAME = "default"
        val DEFAULT_TIMEOUT: Number = 15
        val DEFAULT_RETRY_INTERVAL: Number = 1
    }

    var driver: () -> WebDriver = { FirefoxDriver() }
    var baseUrl = ""
    var atVerifierRequired = false

    var browserManagement = BrowserManagement()

    private var waitPresets: Map<String, WaitPreset> = WaitingDslBuilder().build()
    fun waiting(dsl: WaitingDslBuilder.() -> Unit) {
        waitPresets = WaitingDslBuilder().apply(dsl).build()
    }

    fun getWaitPreset(preset: String) = waitPresets[preset] ?: throw WaitPresetNotFoundException(preset)
    fun getDefaultPreset(): WaitPreset = waitPresets[DEFAULT_PRESET_NAME]!!

    class WaitingDslBuilder {

        private val presets = mutableMapOf<String, WaitPreset>()

        var timeout = DEFAULT_TIMEOUT
        var retryInterval = DEFAULT_RETRY_INTERVAL

        operator fun String.invoke(dsl: WaitPresetDslBuilder.() -> Unit) {
            presets[this] = WaitPresetDslBuilder(timeout, retryInterval).apply(dsl).build()
        }

        fun build(): Map<String, WaitPreset> = CaseInsensitiveMap(
            presets.plus(DEFAULT_PRESET_NAME to WaitPreset(timeout, retryInterval))
        )
    }

    class WaitPresetDslBuilder(var timeout: Number, var retryInterval: Number) {
        fun build() = WaitPreset(timeout, retryInterval)
    }

    private class CaseInsensitiveMap<T>(
        private val decorated: Map<String, T>
    ) : Map<String, T> by decorated.mapKeys({ it.key.toLowerCase() }) {

        override fun get(key: String): T? = decorated[key.toLowerCase()]

        override fun getOrDefault(key: String, defaultValue: T): T =
            decorated.getOrDefault(key.toLowerCase(), defaultValue)

        override fun containsKey(key: String): Boolean = decorated.containsKey(key.toLowerCase())
    }


    class BrowserManagement(

        //TODO put implementation into these classes - so everyone can customize it easily
        var strategy: BrowserManagementStrategy = ReuseSameBrowserAmongTests(),

//        var closeBrowserAfterNTest: Int = -1,
        /** Cost approximately 7s per test
         * Advanced note: Before - is needed for case where previous test had different configuration.
         * */
        var closeBrowserBeforeAndAfterEachTest: Boolean = false,
        /** Cost approximately 15ms per test */
        var clearCookiesAfterEachTest: Boolean = true,
        /** Cost approximately 45ms per test */
        var clearWebStorageAfterEachTest: Boolean = true,
        /**
         *  Close all tabs and windows except one.
         *  If you close windows after test - you can be sure, that this test will not block execution of other tests.
         *  (e.g. by opening dialog windows on exit from page)
         *  */
        var openNewEmptyWindowAndCloseOtherAfterEachTest: Boolean = true


    )

    interface BrowserManagementStrategy

    class CloseBrowser(
        /**
         *  Before test is hand in case, that previous test had different configuration
         *  and you want new browser with your configuration for your test.
         *
         *  Note: closing browser before and after test has no addition costs (closing already closed browser cost nothing).
         *  */
        beforeEachTest: Boolean = true,
        /**
         * Closing browser after your test is good for early detections of errors.
         * If your test somehow prevent browser from closing you will be noticed about it by this test
         * and not by innocent tests which runs after this test.
         *
         *  Note: closing browser before and after test has no addition costs (closing already closed browser cost nothing).
         */
        afterEachTest: Boolean = true
    ) : BrowserManagementStrategy

    class ReuseSameBrowserAmongTests(
        /** Cost approximately 15ms per test */
        var clearCookiesAfterEachTest: Boolean = true,
        /** Cost approximately 45ms per test */
        var clearWebStorageAfterEachTest: Boolean = true,
        /**
         *  Close all tabs and windows except one.
         *  If you close windows after test - you can be sure, that this test will not block execution of other tests.
         *  (e.g. by opening dialog windows on exit from page)
         *  */
        var openNewEmptyWindowAndCloseOtherAfterEachTest: Boolean = true

    ) : BrowserManagementStrategy

}