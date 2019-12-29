package keb.test.util

import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler
import kotlin.properties.Delegates


/**
 * Starts Http Server, which serves content of resource folder (package).
 *
 * Just for for testing purpose.
 *
 * @param resourceFolderToServe e.g. resource folder "keb/testing/multipage"
 */
class HttpResourceFolderServer(val resourceFolderToServe: String) {

    lateinit var vertx: Vertx
    var port by Delegates.notNull<Int>()

    init {
        start()
    }


    private fun start(): HttpResourceFolderServer {
        val vertx = Vertx.vertx()
        val router = Router.router(vertx)
        router.route().handler(StaticHandler.create(resourceFolderToServe))

        port = SocketUtil().findFreePort()
        vertx.createHttpServer().requestHandler(router::accept).listen(port)

        return this
    }


    fun stop(): HttpResourceFolderServer {
        vertx.close()
        TODO()
        return this
    }
}
