package com.gdsc.lineup.location

import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import timber.log.Timber
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by Nakul
 * on 26,April,2022
 */
object SocketHelper {

    private const val SOCKET_URL = "https://arcane-atoll-63814.herokuapp.com/"
    private const val MESSAGE = "setUserLocation"
    private const val LISTENER = "user-joined"

    private var socket: Socket? = null

    fun init() {
        if (socket?.connected() == true)
            return
        try {
            socket = IO.socket(
                SOCKET_URL,
                IO.Options().apply { timeout = TimeUnit.SECONDS.toMillis(120) }
            )
            tryToConnect()
        } catch (e: Exception) {
            Timber.e("exception ${e.message}")
        }
    }

    private var connecting = AtomicBoolean(false)

    private fun tryToConnect() {

        if (socket?.isActive == true || connecting.get())
            return
        connecting.set(true)
        socket?.let {
            it.connect()
            it.on(Socket.EVENT_CONNECT) {
                connecting.set(false)
                Timber.d("Connected!")
                arrayList.forEach { i-> socket?.on(LISTENER,i) }
            }
            it.on(Socket.EVENT_CONNECT_ERROR) { i ->
                connecting.set(false)
                Timber.e(i.getOrNull(0).toString())
                tryToConnect()
            }
            it.on(Socket.EVENT_DISCONNECT) { i ->
                connecting.set(false)
                Timber.e(i.getOrNull(0).toString())
                Timber.e("Disconnected!")
                tryToConnect()
            }
        }
    }

    fun disconnect() {
        socket?.disconnect()
        socket = null
    }

    fun send(message: SocketDataModel) {
        if (socket?.isActive == true)
            socket?.emit(MESSAGE, Gson().toJson(message).toString())
        else
            tryToConnect()
    }

    private val arrayList = arrayListOf<Emitter.Listener>()

    fun collect(listener: Emitter.Listener) {
        socket?.on(LISTENER, listener)
        if (!arrayList.contains(listener))
        arrayList.add(listener)
    }

    fun stopCollection(listener: Emitter.Listener) {
        socket?.off(LISTENER, listener)
        arrayList.remove(listener)
    }
}