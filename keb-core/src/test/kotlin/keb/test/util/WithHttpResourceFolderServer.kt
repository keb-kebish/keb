package keb.test.util

import com.horcacorp.testing.keb.core.Browser
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

interface WithHttpResourceFolderServer {
    val server: HttpResourceFolderServer
    val browser: Browser

    @BeforeEach
    fun start() {
        server.start()
        browser.config.baseUrl = server.baseUrl
    }

    @AfterEach
    fun stop() {
        server.stop()
    }

}