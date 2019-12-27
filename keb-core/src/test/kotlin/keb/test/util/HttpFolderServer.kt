package keb.test.util

import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler


/**
 * Starts Http Server, which serves content of resource folder (package).
 *
 * Just for for testing purpose.
 *
 * @param resourceFolderToServe e.g. resource folder "keb/testing/multipage"
 */
class HttpFolderServer(val resourceFolderToServe: String) {

    lateinit var vertx: Vertx


    init {
        start()
    }


    private fun start(): HttpFolderServer {
        val vertx = Vertx.vertx();
        val router = Router.router(vertx);
        router.route().handler(StaticHandler.create(resourceFolderToServe));
        vertx.createHttpServer().requestHandler(router::accept).listen(8080);   //TODO free port
        return this
    }

    val port: Int
        get() {
            return 8080
        }


    fun stop(): HttpFolderServer {
        vertx.close()
        TODO()
        return this
    }
}
