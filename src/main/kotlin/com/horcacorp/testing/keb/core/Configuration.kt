@file:Suppress("PropertyName")

package com.horcacorp.testing.keb.core

fun kebConfig(conf: Configuration.() -> Unit) = Configuration().apply { conf() }

class Configuration {

    private val DEFAULT_WAIT_TIMEOUT = 15000L
    private val DEFAULT_WAIT_RETRY_INTERVAL = 250L

    val DEFAULT_WAIT_PRESET_NAME = "DEFAULT"

    var elementsFetchType = ContentFetchType.ON_EVERY_ACCESS
    var modulesFetchType = ContentFetchType.ON_FIRST_ACCESS
    var browserType = BrowserType.FIREFOX
    var urlPrefix = ""
    val waitPresets = mutableMapOf(
        DEFAULT_WAIT_PRESET_NAME to WaitPreset(DEFAULT_WAIT_TIMEOUT, DEFAULT_WAIT_RETRY_INTERVAL)
    )

    fun addPreset(
        name: String,
        timeoutMillis: Long = DEFAULT_WAIT_TIMEOUT,
        retryIntervalMillis: Long = DEFAULT_WAIT_RETRY_INTERVAL
    ) {
        waitPresets[name] = WaitPreset(timeoutMillis, retryIntervalMillis)
    }

}