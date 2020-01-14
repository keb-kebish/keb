package org.kebish.usage.test.util

import java.net.ServerSocket

class SocketUtil {

    fun findFreePort(): Int {
        val socket = ServerSocket(0)
        val port = socket.localPort
        socket.close()
        return port
    }

}
