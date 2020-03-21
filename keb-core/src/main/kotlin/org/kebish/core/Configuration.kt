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
//        var closeBrowserAfterNTest: Int = -1,
        /** Cost approximately 7s per test */
        var closeBrowserAfterEachTest: Boolean = false,
        /** Cost approximately 15ms per test */
        var clearCookiesAfterEachTest: Boolean = true,
        /** Cost approximately 45ms per test */
        var clearWebStorageAfterEachTest: Boolean = true //todo Geb has false



    )
}