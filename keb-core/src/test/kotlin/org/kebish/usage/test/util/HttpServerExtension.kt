package org.kebish.usage.test.util

import org.kebish.core.browser.Browser
import org.kebish.usage.test.util.extendable.Extension

/**
 *  Starts Testing server. And set "baseUrl" to browser.config
 *
 * @param resourceFolderToServe Resource directory to serve.
 */
class HttpResourceFolderServerExtension(
    browser: Browser,
    resourceFolderToServe: String
) : HttpServerExtension(browser) {
    override val server = HttpResourceFolderServer(resourceFolderToServe)
}

class HttpBuilderServerExtension(
    browser: Browser,
    vararg content: HtmlContent
) : HttpServerExtension(browser) {
    override val server = HttpBuilderServer(*content)
}

abstract class HttpServerExtension(private val browser: Browser) : Extension {

    abstract val server: HttpServer

    override fun extBeforeEach() {
        server.start()
        browser.baseUrl = server.baseUrl
    }

    override fun extAfterEach() {
        server.stop()
    }
}