package org.kebish.usage.test.util

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.content.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.html.HTML
import kotlin.properties.Delegates


/**
 * Starts Http Server, which serves content of resource folder (package).
 *
 * Just for for testing purpose.
 *
 * @param resourceFolderToServe e.g. resource folder "keb/testing/multipage"
 * @param serveOnRoot which static file to serve when accessing server root - i.e. "http://localhost:8080/".
 *   This file must be present within the resource folder. Defaults to "index.html".
 */
class HttpResourceFolderServer(
    private val resourceFolderToServe: String,
    private val serveOnRoot: String = "index.html"
) : KtorHttpServer({
    routing {
        static {
            resource("/", serveOnRoot, resourceFolderToServe)
            resources(resourceFolderToServe)
        }
    }
})

class HttpBuilderServer(private vararg val content: HtmlContent) : KtorHttpServer({
    routing {
        content.forEach { (path, htmlBuilder) ->
            get(path) {
                call.respondHtml { htmlBuilder() }
            }
        }
    }
})

data class HtmlContent(
    val path: String = "/",
    val htmlBuilder: HTML.() -> Unit
)


abstract class KtorHttpServer(private val initializer: Application.() -> Unit) : HttpServer {

    private lateinit var server: ApplicationEngine
    private var port by Delegates.notNull<Int>()

    override val baseUrl get() = "http://localhost:$port/"

    override fun start() {
        port = SocketUtil().findFreePort()
        server = embeddedServer(Netty, port = port) { initializer() }.start()
    }

    override fun stop() {
        server.stop(1000, 1000)
    }
}

interface HttpServer {
    val baseUrl: String
    fun start()
    fun stop()
}