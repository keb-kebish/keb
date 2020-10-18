package org.kebish.core.config

import org.kebish.core.browser.Browser

public interface Reporter {
    public fun report(testInfo: TestInfo, browser: Browser)

    /** Configuration call set this. So that reportsDir can be shared between configurations */
    public fun setConfig(reports: Configuration.Reports)
}