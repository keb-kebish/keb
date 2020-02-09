package org.kebish.usage.test.util

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.http.content.resource
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.html.HTML
import java.util.concurrent.TimeUnit
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
        content.forEach { (content, path) ->
            get(path) {
                call.respondHtml { content() }
            }
        }
    }
})

data class HtmlContent(
    val content: HTML.() -> Unit,
    val path: String = "/"
)


abstract class KtorHttpServer(private val initializer: Application.() -> Unit) : HttpServer {

    private lateinit var server: ApplicationEngine
    private var port by Delegates.notNull<Int>()

    override val baseUrl get() = "http://localhost:$port/"

    override fun start() {
        port = SocketUtil().findFreePort()
        server = embeddedServer(Netty, port = port) {
            initializer()
        }.start()
    }

    override fun stop() {
        server.stop(1, 1, TimeUnit.SECONDS)
    }
}

interface HttpServer {
    val baseUrl: String
    fun start()
    fun stop()
}