package keb.test.util

import com.horcacorp.testing.keb.core.Browser
import keb.test.util.extendable.Extension

/**
 *  Starts Testing server. And set "baseUrl" to browser.config
 *
 * @param resourceFolderToServe Resource directory to serve.
 */
class HttpResourceFolderServerExtension(val resourceFolderToServe: String, val browser: Browser) : Extension {
    val server: HttpResourceFolderServer = HttpResourceFolderServer(resourceFolderToServe)

    override fun extBeforeEach() {
        server.start()
        browser.baseUrl = server.baseUrl
    }

    override fun extAfterEach() {
        server.stop()
    }

}