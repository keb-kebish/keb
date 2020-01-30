package org.kebish.core

import org.openqa.selenium.WebElement

interface WaitSupport {

    val browser: Browser

    val defaultWaitPreset get() = browser.config.getDefaultPreset()

    fun <T> waitFor(
        config: WaitConfig,
        desc: String? = null,
        f: () -> T?
    ): T? {
        return if (config.shouldWait) {
            waitFor(config.getPreset(browser), desc, f)
        } else {
            f()
        }
    }

    fun <T> waitFor(
        preset: String?,
        desc: String? = null,
        f: () -> T?
    ) = waitFor(preset?.let { browser.config.getWaitPreset(it) } ?: defaultWaitPreset, desc, f)

    fun <T> waitFor(
        preset: WaitPreset,
        desc: String? = null,
        f: () -> T?
    ) = waitFor(preset.timeout, preset.retryInterval, desc, f)

    fun <T> waitFor(
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

}

class WaitTimeoutMessageBuilder(private val timeoutedAfter: Number) {

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

data class WaitPreset(val timeout: Number, val retryInterval: Number)

abstract class WaitConfig {
    open val shouldWait = true
    abstract fun getPreset(browser: Browser): WaitPreset

    companion object {
        fun from(config: Any?) = when (config) {
            is Number -> TimeoutWaitConfig(config)
            is WaitPreset -> PresetWaitConfig(config)
            is String -> StringWaitConfig(config)
            is Boolean -> if (config) DefaultPresetWaitConfig() else NoOpWaitConfig()
            null -> NoOpWaitConfig()
            else -> throw IllegalArgumentException("Unexpected wait configuration parameter '$config'.")
        }
    }
}

class TimeoutWaitConfig(private val timeout: Number) : WaitConfig() {
    override fun getPreset(browser: Browser) = browser.defaultWaitPreset.copy(timeout = timeout)
}

class PresetWaitConfig(private val preset: WaitPreset) : WaitConfig() {
    override fun getPreset(browser: Browser) = preset
}

class StringWaitConfig(private val preset: String) : WaitConfig() {
    override fun getPreset(browser: Browser) = browser.config.getWaitPreset(preset)
}

class DefaultPresetWaitConfig : WaitConfig() {
    override fun getPreset(browser: Browser) = browser.defaultWaitPreset
}

class NoOpWaitConfig : WaitConfig() {
    override val shouldWait = false
    override fun getPreset(browser: Browser): WaitPreset {
        throw IllegalStateException("Unexpected error, did not expect waitFor to be invoked.")
    }
}

class WaitTimeoutException(msg: String, cause: Throwable?) : RuntimeException(msg, cause)
class WaitPresetNotFoundException(presetName: String) : RuntimeException("Preset with name '$presetName' not found.")