package keb.test.util

import io.ktor.http.content.resource
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
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
class HttpResourceFolderServer(val resourceFolderToServe: String, private val serveOnRoot: String = "index.html") {

    lateinit var server: ApplicationEngine
    var port by Delegates.notNull<Int>()
    val baseUrl get() = "http://localhost:$port/"


    fun start() = apply {
        port = SocketUtil().findFreePort()
        server = embeddedServer(Netty, port = port) {
            routing {
                static {
                    resource("/", "$resourceFolderToServe/$serveOnRoot")
                    resources(resourceFolderToServe)
                }
            }
        }.start()
    }

    fun stop() = apply {
        server.stop(1, 1, TimeUnit.SECONDS)
    }

    fun with(closure: (HttpResourceFolderServer) -> Unit) {
        try {
            closure(this)
        } finally {
            stop()
        }
    }
}
