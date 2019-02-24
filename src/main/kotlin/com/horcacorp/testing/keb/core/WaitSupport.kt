package com.horcacorp.testing.keb.core

interface WaitSupport {

    fun <T> waitFor(waitParam: Any = true, desc: String? = null, f: () -> T): T

    fun <T> waitFor(presetName: String, desc: String?, f: () -> T): T

    fun <T> waitFor(timeoutMillis: Long, retryIntervalMillis: Long, desc: String? = null, f: () -> T): T

}

data class WaitPreset(val timeoutMillis: Long, val retryIntervalMillis: Long)

class WaitTimeoutException(msg: String, cause: Throwable?) : RuntimeException(msg, cause)
class WaitPresetNotFoundException(presetName: String) : RuntimeException("Preset with name '$presetName' not found.")