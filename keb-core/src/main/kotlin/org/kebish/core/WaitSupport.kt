package org.kebish.core

import org.kebish.core.browser.Browser
import org.kebish.core.config.Configuration
import org.kebish.core.content.EmptyContent
import org.kebish.core.module.Module
import org.openqa.selenium.WebElement

public interface WaitSupport {

    public val browser: Browser

    private val defaultWaitPreset get() = browser.config.getDefaultPreset()

    public fun <T> waitFor(
        preset: String?,
        desc: String? = null,
        f: () -> T?
    ): T = waitFor(preset?.let { browser.config.getWaitPreset(it) } ?: defaultWaitPreset, desc, f)

    public fun <T> waitFor(
        preset: WaitPreset,
        desc: String? = null,
        f: () -> T?
    ): T = waitFor(preset.timeout, preset.retryInterval, desc, f)

    public fun <T> waitFor(
        timeout: Number = defaultWaitPreset.timeout,
        retryInterval: Number = defaultWaitPreset.retryInterval,
        desc: String? = null,
        f: () -> T?
    ): T {
        val timeoutMillis = timeout.toMillis()
        val retryIntervalMillis = retryInterval.toMillis()
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
            val err = WaitTimeoutMessageBuilder(timeout)
                .withDetail(desc)
                .withLastEvaluatedValue(value)
                .withLastThrown(thrown)
                .build()
            throw WaitTimeoutException(err, thrown)
        }
    }

    private fun Number.toMillis() = toDouble().times(1000).toLong()

    private fun resolveTruthiness(value: Any?): Boolean {
        return when (value) {
            is Number -> value != 0
            is CharSequence -> value.length > 0
            is Boolean -> value
            is Collection<*> -> if (value.isEmpty()) false else value.all { resolveTruthiness(it) }
            is WebElement -> value.location != null
            is Module -> value.scope?.let { resolveTruthiness(it) } ?: true
            is EmptyContent, null -> false
            else -> true
        }
    }

    private class WaitTimeoutMessageBuilder(private val timeoutedAfter: Number) {

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
            lastThrown?.let { this.lastThrowableMessage = " Last exception cause: '${it.message}'." }
        }

        fun build() =
            "Waiting$detail has timed out after $timeoutedAfter seconds.$lastEvaluatedValue$lastThrowableMessage"

    }

}

public data class WaitPreset(val timeout: Number, val retryInterval: Number)

internal class WaitPresetFactory {
    fun from(value: Any, configuration: Configuration) = when (value) {
        is Number -> configuration.getDefaultPreset().copy(timeout = value)
        is String -> configuration.getWaitPreset(value)
        is WaitPreset -> value
        is Boolean -> if (value) configuration.getDefaultPreset() else null
        else -> throw IllegalStateException("Unable to create wait preset from '${value.javaClass.simpleName}'.")
    }
}

public class WaitTimeoutException(msg: String, cause: Throwable?) : RuntimeException(msg, cause)
public class WaitPresetNotFoundException(presetName: String) :
    RuntimeException("Preset with name '$presetName' not found.")