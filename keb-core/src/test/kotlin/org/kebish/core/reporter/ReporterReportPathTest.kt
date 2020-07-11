package org.kebish.core.reporter

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.io.TempDir
import org.kebish.core.Browser
import org.kebish.core.config.TestInfo
import org.kebish.core.kebConfig
import org.kebish.junit5.KebTest
import org.kebish.usage.test.util.extendable.Extendable
import org.kebish.usage.test.util.extendable.ExtendableImpl
import java.io.File
import java.nio.file.Path

class PathTestReporter : ReportsDirReporter() {
    lateinit var testInfo: TestInfo

    override fun report(testInfo: TestInfo, browser: Browser) {
        this.testInfo = testInfo
    }

}


@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ReporterReportPathTest : KebTest(kebConfig { }),
    Extendable by ExtendableImpl() {

    private val pathTestReporter = PathTestReporter()

    // Given
    @BeforeAll
    fun `clean reports directory`() {
        config.driver = { throw IllegalStateException("This test cannot initialize driver") }
        config.reports.reporterDir = File("build/keb-reports-just-for-ReporterReportPath")
        config.reports.afterEachTestReporters.add(pathTestReporter)
    }

    // When - test is successful
    @Test
    @Order(1)
    @Suppress("NonAsciiCharacters")
    fun `žluťoučký kůň`(@TempDir tmpDri: Path) {

    }

    // Then
    @AfterAll
    fun `test reporter called with path as expected`() {
        assertThat(pathTestReporter.testInfo.reportPath).isEqualTo("org/kebish/core/reporter/ReporterReportPathTest.žluťoučký kůň()")

    }


}