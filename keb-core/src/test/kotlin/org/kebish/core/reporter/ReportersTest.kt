package org.kebish.core.reporter

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.then
import org.junit.jupiter.api.Test
import org.kebish.core.Configuration

class ReportersTest {

    @Test
    fun `after registration reporter get configuration - success reporter`() {
        // given
        val reports = Configuration.Reports()
        val successReporter = mock<Configuration.Reporter>()

        // when
        reports.testSuccessReporters.add(successReporter)

        // then
        then(successReporter).should().setConfig(reports)
    }

    @Test
    fun `after registration reporter get configuration - fail reporter`() {
        // given
        val reports = Configuration.Reports()
        val failReporter = mock<Configuration.Reporter>()

        // when
        reports.testFailReporters.add(failReporter)

        // then
        then(failReporter).should().setConfig(reports)
    }

    @Test
    fun `after registration reporter get configuration - afterTest reporter`() {
        // given
        val reports = Configuration.Reports()
        val afterTestReporter = mock<Configuration.Reporter>()

        // when
        reports.afterEachTestReporters.add(afterTestReporter)

        // then
        then(afterTestReporter).should().setConfig(reports)
    }
}