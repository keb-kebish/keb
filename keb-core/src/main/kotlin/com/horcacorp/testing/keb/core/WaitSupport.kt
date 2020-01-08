package com.horcacorp.testing.keb.core

import org.openqa.selenium.WebElement

interface WaitSupport {

    val browser: Browser

    fun <T> waitFor(presetName: String? = null, desc: String? = null, f: () -> T?): T {
        val preset = presetName
            ?.let { browser.config.waitPresets[it.toUpperCase()] ?: throw WaitPresetNotFoundException(it) }
            ?: browser.config.getDefaultPreset()
        return waitFor(preset.timeoutMillis, preset.retryIntervalMillis, desc, f)
    }

    fun <T> waitFor(timeoutMillis: Long, retryIntervalMillis: Long, desc: String? = null, f: () -> T?): T {
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
            val err = if (desc != null) {
                "Waiting for '$desc' has timed out after $timeoutMillis milliseconds."
            } else {
                "Waiting has timed out after $timeoutMillis milliseconds."
            }
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

data class WaitPreset(val timeoutMillis: Long, val retryIntervalMillis: Long)

class WaitTimeoutException(msg: String, cause: Throwable?) : RuntimeException(msg, cause)
class WaitPresetNotFoundException(presetName: String) : RuntimeException("Preset with name '$presetName' not found.")