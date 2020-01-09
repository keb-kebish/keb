package com.horcacorp.testing.keb.core

import org.openqa.selenium.WebElement

interface WaitSupport {

    val browser: Browser

    val defaultWaitPreset get() = browser.config.getDefaultPreset()

    fun <T> waitFor(
        presetName: String? = null,
        desc: String? = null,
        f: () -> T?
    ): T {
        val preset = presetName
            ?.let { browser.config.waitPresets[it.toUpperCase()] ?: throw WaitPresetNotFoundException(it) }
            ?: defaultWaitPreset
        return waitFor(preset.timeoutMillis, preset.retryIntervalMillis, desc, f)
    }

    fun <T> waitFor(
        timeoutMillis: Long = defaultWaitPreset.timeoutMillis,
        retryIntervalMillis: Long = defaultWaitPreset.retryIntervalMillis,
        desc: String? = null,
        f: () -> T?
    ): T {
        val timeoutAt = System.currentTimeMillis() + timeoutMillis
        var passed = false
        var value: T? = null
        var thrown: Throwable? = null

        try {
            value = f()
            passed = resolveTruthiness(value)
        } catch (t: Throwable) {
            thrown = t
        }

        var timedOut = System.currentTimeMillis() > timeoutAt
        while (!passed && !timedOut) {
            Thread.sleep(retryIntervalMillis)
            try {
                value = f()
                passed = resolveTruthiness(value)
                thrown = null
            } catch (t: Throwable) {
                thrown = t
            } finally {
                timedOut = System.currentTimeMillis() > timeoutAt
            }
        }

        return if (passed) {
            value!!
        } else {
            val err = WaitTimeoutMessageBuilder(timeoutMillis)
                .withDetail(desc)
                .withLastEvaluatedValue(value)
                .withLastThrown(thrown)
                .build()
            throw WaitTimeoutException(err, thrown)
        }
    }

    private fun resolveTruthiness(value: Any?): Boolean {
        return when (value) {
            is Number -> value != 0
            is CharSequence -> value.length > 0
            is Boolean -> value
            is Collection<*> -> if (value.isEmpty()) false else value.all { resolveTruthiness(it) }
            is WebElement -> value.location != null
            is Module -> value.scope?.let { resolveTruthiness(it) } ?: true
            null -> false
            else -> true
        }
    }

}

class WaitTimeoutMessageBuilder(private val timeoutedAfterMillis: Long) {

    private var detail: String = ""
    private var lastEvaluatedValue: String = ""
    private var lastThrowableMessage = ""

    fun withDetail(detail: String?) = apply {
        detail?.let { this.detail = " for '$it'" }
    }

    fun withLastEvaluatedValue(lastEvaluatedValue: Any?) = apply {
        this.lastEvaluatedValue = " Last evaluated value: '$lastEvaluatedValue'."
    }

    fun withLastThrown(lastThrown: Throwable?) = apply {
        lastThrown?.let { " Last exception cause: '${it.message}'." }
    }

    fun build() =
        "Waiting$detail has timed out after ${timeoutedAfterMillis / 1000} seconds.$lastEvaluatedValue$lastThrowableMessage"

}

data class WaitPreset(val timeoutMillis: Long, val retryIntervalMillis: Long)

class WaitTimeoutException(msg: String, cause: Throwable?) : RuntimeException(msg, cause)
class WaitPresetNotFoundException(presetName: String) : RuntimeException("Preset with name '$presetName' not found.")