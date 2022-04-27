package com.gdsc.lineup.location

import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.delay
import timber.log.Timber
import java.lang.Exception
import java.net.URI

/**
 * Created by Nakul
 * on 26,April,2022
 */
object SocketHelper {

    private const val SOCKET_URL = "https://line-up-server.herokuapp.com/"
    private const val MESSAGE = "setUserLocation"
    private const val LISTENER = "user-joined"

    private var socket: Socket? = null

    fun init() {
        if (socket?.connected() == true)
            return
        try {
            socket = IO.socket(SOCKET_URL)
            tryToConnect()
        }
        catch(e: Exception) {
            Timber.e("exception ${e.message}")
        }
    }

    private fun tryToConnect() {
        socket?.let {
            it.connect()
            it.on(Socket.EVENT_CONNECT) {
                Timber.d("Connected!")
            }
            it.on(Socket.EVENT_CONNECT_ERROR) { i ->
                Timber.e(i.getOrNull(0).toString())
                it.connect()
            }
            it.on(Socket.EVENT_DISCONNECT) { i ->
                Timber.e(i.getOrNull(0).toString())
                it.connect()
            }
        }
    }

    fun disconnect() {
        socket?.disconnect()
        socket = null
    }

    fun send(message: SocketDataModel) {
        socket?.emit(MESSAGE, message)
    }

    fun collect(listener: Emitter.Listener) = socket?.on(LISTENER, listener)

    fun stopCollection(listener: Emitter.Listener) = socket?.off(LISTENER, listener)
}