@file:Suppress("PropertyName", "MayBeConstant", "MapGetWithNotNullAssertionOperator")

package org.kebish.core.config

import org.kebish.core.WaitPreset
import org.kebish.core.WaitPresetNotFoundException
import org.kebish.core.browser.provider.BrowserProvider
import org.kebish.core.browser.provider.StaticBrowserProvider
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import java.io.File

public fun kebConfig(conf: Configuration.() -> Unit): Configuration = Configuration().apply { conf() }

public class Configuration {

    public companion object {
        public val DEFAULT_PRESET_NAME: String = "default"
        public val DEFAULT_TIMEOUT: Number = 15
        public val DEFAULT_RETRY_INTERVAL: Number = 1
    }

    public var driver: () -> WebDriver = { FirefoxDriver() }
    public var baseUrl: String = ""
    public var atVerifierRequired: Boolean = false

    /** Keb support have two providers
     * NewBrowserForEachTestProvider - which quit WebDriver (close browser) after each test
     * StaticBrowserProvider - which reuse same browser among tests
     * */
    public var browserProvider: BrowserProvider = StaticBrowserProvider(
        clearCookiesAfterEachTest = true,
        clearWebStorageAfterEachTest = false,
        openNewEmptyWindowAndCloseOtherAfterEachTest = true,
        autoCloseAlerts = true
    )

    private var waitPresets: Map<String, WaitPreset> = WaitingDslBuilder().build()
    public fun waiting(dsl: WaitingDslBuilder.() -> Unit) {
        waitPresets = WaitingDslBuilder().apply(dsl).build()
    }

    public fun getWaitPreset(preset: String): WaitPreset =
        waitPresets[preset] ?: throw WaitPresetNotFoundException(preset)

    public fun getDefaultPreset(): WaitPreset = waitPresets[DEFAULT_PRESET_NAME]!!

    public class WaitingDslBuilder {

        private val presets = mutableMapOf<String, WaitPreset>()

        public var timeout: Number = DEFAULT_TIMEOUT
        public var retryInterval: Number = DEFAULT_RETRY_INTERVAL

        public operator fun String.invoke(dsl: WaitPresetDslBuilder.() -> Unit) {
            presets[this] = WaitPresetDslBuilder(timeout, retryInterval).apply(dsl).build()
        }

        public fun build(): Map<String, WaitPreset> = CaseInsensitiveMap(
            presets.plus(DEFAULT_PRESET_NAME to WaitPreset(timeout, retryInterval))
        )
    }

    public class WaitPresetDslBuilder(public var timeout: Number, public var retryInterval: Number) {
        public fun build(): WaitPreset = WaitPreset(timeout, retryInterval)
    }

    private class CaseInsensitiveMap<T>(
        private val decorated: Map<String, T>
    ) : Map<String, T> by decorated.mapKeys({ it.key.toLowerCase() }) {

        override fun get(key: String): T? = decorated[key.toLowerCase()]

        override fun getOrDefault(key: String, defaultValue: T): T =
            decorated.getOrDefault(key.toLowerCase(), defaultValue)

        override fun containsKey(key: String): Boolean = decorated.containsKey(key.toLowerCase())
    }

    public val reports: Reports = Reports()

    public fun reports(dsl: Reports.() -> Unit) {
        reports.apply(dsl)
    }

    public class Reports(
        /** Directory for reports. For reporters which use it. */
        public var reporterDir: File = File("keb-reports")

    ) {
        public val testFailReporters: ReportersList = ReportersList(this)
        public val testSuccessReporters: ReportersList = ReportersList(this)
        public val afterEachTestReporters: ReportersList = ReportersList(this)
    }


    public class ReportersList(public val reports: Reports) : Iterable<Reporter> {

        public val list: MutableList<Reporter> = mutableListOf()

        public fun add(reporter: Reporter) {
            reporter.setConfig(reports)
            list.add(reporter)
        }

        public fun clear(): Unit = list.clear()

        public fun remove(reporter: Reporter): Boolean = list.remove(reporter)

        public fun getAll(): List<Reporter> = object : List<Reporter> by list {}

        override fun iterator(): Iterator<Reporter> = list.iterator()


    }


}