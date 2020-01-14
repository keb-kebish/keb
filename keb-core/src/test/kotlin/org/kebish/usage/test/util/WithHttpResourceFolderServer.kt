package org.kebish.usage.test.util

import org.kebish.core.Browser
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

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