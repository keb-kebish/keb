package keb.test.util

import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler
import java.nio.file.Path



/**
 * Starts Http Server, which serves content of folder.
 *
 * Just for for testing purpose.
 */
class HttpFolderServer(val folderToServe: Path) {

//    init {
//        start()
//    }




    fun start(): HttpFolderServer {
        val vertx = Vertx.vertx();

        val router = Router.router(vertx);
//        router.route().handler(StaticHandler.create(folderToServe.toAbsolutePath().normalize().toString()));
//        router.route().handler(StaticHandler.create("keb.testing.multipages"));
        router.route().handler(StaticHandler.create("keb/testing/multipages"));
//        router.route().handler(StaticHandler.create("client"));
//        router.route("/hello*").handler(StaticHandler.create("keb.testing.multipages"));

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
//        TODO()

        println("localhost:8080/page1.html")
        return this
    }

    val port: Int
        get() {
            return 8080
        }


    fun stop(): HttpFolderServer {
        TODO()
        return this
    }
}
