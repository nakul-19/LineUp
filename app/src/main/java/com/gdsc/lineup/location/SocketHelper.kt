package com.gdsc.lineup.location

import android.util.Log
import com.github.nkzawa.engineio.client.transports.WebSocket
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import java.net.URI

/**
 * Created by Nakul
 * on 26,April,2022
 */
object SocketHelper {

    private const val SOCKET_URL = ""
    private const val MESSAGE = "MESSAGE"
    private const val TAG = "SocketHelper"

    private var socket: Socket? = null

    fun init() {
        if (socket?.connected() == true)
            return
        val uri = URI.create(SOCKET_URL)
        val options = IO.Options().apply {
            timeout = 60000
            transports = arrayOf(WebSocket.NAME)
        }
        socket = IO.socket(uri, options)
        tryToConnect()
    }

    private fun tryToConnect() {
        socket?.let {
            it.connect()
            it.on(Socket.EVENT_CONNECT) {
                Log.d(TAG, "Connected!")
            }
            it.on(Socket.EVENT_CONNECT_ERROR) { i ->
                Log.e(TAG, i.toString())
            }
            it.on(Socket.EVENT_CONNECT_TIMEOUT) { i ->
                Log.e(TAG, i.toString())
                it.connect()
            }
        }
    }

    fun disconnect() {
        socket?.disconnect()
        socket = null
    }

    fun send(message: String) {
        socket?.emit(MESSAGE, message)
    }

    fun collect(listener: (i: Array<out Any>) -> Unit) = socket?.on(MESSAGE) { listener(it) }

    fun stopCollection() = socket?.off(MESSAGE)
}