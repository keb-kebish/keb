package org.kebish.usage.test.util

import org.kebish.core.Browser
import org.kebish.usage.test.util.extendable.Extension

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