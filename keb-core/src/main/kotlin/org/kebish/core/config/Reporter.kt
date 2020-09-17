package org.kebish.core.config

import org.kebish.core.browser.Browser

interface Reporter {
    fun report(testInfo: TestInfo, browser: Browser)

    /** Configuration call set this. So that reportsDir can be shared between configurations */
    fun setConfig(reports: Configuration.Reports)
}