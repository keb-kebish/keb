package keb.test.util

import com.horcacorp.testing.keb.core.Browser
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

private lateinit var _server: HttpResourceFolderServer

interface WithHttpResourceFolderServer {

    val server2: HttpResourceFolderServer = HttpResourceFolderServer("")


    val server: HttpResourceFolderServer
        get() = if (!::_server.isInitialized) {
            HttpResourceFolderServer("xxx").also { _server = it }
        } else {
            _server
        }
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