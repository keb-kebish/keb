package org.kebish.core.tool

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kebish.core.Browser
import org.kebish.test.config.commonTestKebConfig
import java.io.File
import java.nio.file.Files

internal class ScreenshotMakerTest {

    val browser = Browser(commonTestKebConfig())

    @BeforeEach
    fun before() {
        browser.driver.get("http://kebish.org")
    }

    @AfterEach
    fun after() {
        browser.quit()
    }

    @Test
    fun takeScreenshot() {
        // given
        val destination = createTmpFile()

        // when
        ScreenshotMaker(browser).takeScreenshot(destination)

        // then
        assertThat(destination.length()).isGreaterThan(5 * 1024)

    }

    private fun createTmpFile(): File {
        val createTempFile = Files.createTempFile("Keb_Screenshot_", ".png").toFile()
        createTempFile.deleteOnExit()
        return createTempFile
    }
}