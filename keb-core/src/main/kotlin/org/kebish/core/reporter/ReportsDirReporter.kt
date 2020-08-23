package org.kebish.core.reporter

import org.kebish.core.Configuration
import org.kebish.core.config.Reporter
import java.io.File

abstract class ReportsDirReporter : Reporter {
    protected lateinit var reportsConfig: Configuration.Reports

    override fun setConfig(reports: Configuration.Reports) {
        this.reportsConfig = reports
    }

    protected fun resolvedReportsDir(): File {
        val reportsDir = reportsConfig.reporterDir
        reportsDir.mkdirs()
        return reportsDir
    }
}