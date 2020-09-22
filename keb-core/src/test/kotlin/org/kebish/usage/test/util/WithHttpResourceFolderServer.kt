package org.kebish.usage.test.util

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.kebish.core.browser.Browser

interface WithHttpResourceFolderServer {
    val server: HttpResourceFolderServer
    val browser: Browser

    @BeforeEach
    fun start() {
        server.start()
        browser.baseUrl = server.baseUrl
    }

    @AfterEach
    fun stop() {
        server.stop()
    }

}