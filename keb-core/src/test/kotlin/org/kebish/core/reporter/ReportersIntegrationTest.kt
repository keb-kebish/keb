package org.kebish.core.reporter

import com.nhaarman.mockito_kotlin.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.kebish.core.Configuration
import org.kebish.junit5.KebTest
import org.kebish.test.config.commonTestKebConfig
import org.kebish.usage.test.util.extendable.Extendable
import org.kebish.usage.test.util.extendable.ExtendableImpl

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ReportersIntegrationTest : KebTest(commonTestKebConfig())
    , Extendable by ExtendableImpl() {

    // Given - config with reporters
    val successReporter = mock<Configuration.Reporter>()
    val failReporter = mock<Configuration.Reporter>()
    val afterTestReporter = mock<Configuration.Reporter>()

    init {
        config.reports.testSuccessReporters.add(successReporter)
        config.reports.testFailReporters.add(failReporter)
        config.reports.afterEachTestReporters.add(afterTestReporter)
    }

    // When -
    @Test
    @Order(1)
    fun `success test`() {

    }

    // Then
    @Test
    @Order(2)
    fun `success and after reporter called, failed reporter not called`() {

        inOrder(successReporter, afterTestReporter) {
            verify(successReporter).report(any(), any())
            verify(afterTestReporter).report(any(), any())
        }
        verify(failReporter, times(0)).report(any(), any())
    }

}