@file:Suppress("PropertyName", "MayBeConstant", "MapGetWithNotNullAssertionOperator")

package com.horcacorp.testing.keb.core

import org.openqa.selenium.WebDriver

fun kebConfig(conf: Configuration.() -> Unit) = Configuration().apply { conf() }

class Configuration {

    companion object {
        val DEFAULT_PRESET_NAME = "default"
        val DEFAULT_TIMEOUT: Number = 15
        val DEFAULT_RETRY_INTERVAL: Number = 1
    }

    var driver: WebDriver? = null
    var baseUrl = ""

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

        fun preset(alias: String, dsl: WaitPresetDslBuilder.() -> Unit) {
            presets[alias] = WaitPresetDslBuilder(timeout, retryInterval).apply(dsl).build()
        }

        fun build(): Map<String, WaitPreset> = CaseInsensitiveMap(
            presets.plus(DEFAULT_PRESET_NAME to WaitPreset.fromSeconds(timeout, retryInterval))
        )
    }

    class WaitPresetDslBuilder(var timeout: Number, var retryInterval: Number) {
        fun build() = WaitPreset.fromSeconds(timeout, retryInterval)
    }

    private class CaseInsensitiveMap<T>(
        private val decorated: Map<String, T>
    ) : Map<String, T> by decorated.mapKeys({ it.key.toLowerCase() }) {

        override fun get(key: String) = decorated[key.toLowerCase()]

        override fun getOrDefault(key: String, defaultValue: T) =
            decorated.getOrDefault(key.toLowerCase(), defaultValue)

        override fun containsKey(key: String) = decorated.containsKey(key.toLowerCase())
    }
}