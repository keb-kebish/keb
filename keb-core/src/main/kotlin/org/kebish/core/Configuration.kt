@file:Suppress("PropertyName", "MayBeConstant", "MapGetWithNotNullAssertionOperator")

package org.kebish.core

import org.kebish.core.browser.provider.BrowserProvider
import org.kebish.core.browser.provider.StaticBrowserProvider
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

    /** Keb support have two providers
     * NewBrowserForEachTestProvider - which quit WebDriver (close browser) after each test
     * StaticBrowserProvider - which reuse same browser among tests
     * */
    var browserProvider: BrowserProvider = StaticBrowserProvider(
        clearCookiesAfterEachTest = true,
        clearWebStorageAfterEachTest = false,
        openNewEmptyWindowAndCloseOtherAfterEachTest = true
    )

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


}