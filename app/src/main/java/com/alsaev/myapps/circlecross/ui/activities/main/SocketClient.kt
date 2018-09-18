package com.alsaev.myapps.circlecross.ui.activities.main

import android.util.Log
import java.io.*
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket

const val MA_PORT = 6969

private const val OUT_EXIT = "EXIT"
private const val OUT_MESSAGE = "MESSAGE"

private const val IN_MESSAGE = "MESSAGE"
private const val IN_LIST_OF_CLIENTS = "LIST_OF_CLIENTS"
private const val IN_ADD_CLIENT = "ADD_CLIENT"
private const val IN_REMOVE_CLIENT = "REMOVE_CLIENT"

class SocketClient() : Thread() {
    private var isRunning = false

    private var writerStream: PrintWriter? = null
    private var readerStream: BufferedReader? = null
    private var listener: SocketListener? = null

    override fun run() {
        try {
            Log.d("socket", "try to find server")
            val address = InetAddress.getByName("10.0.0.102")

            try {
                Log.d("socket", "try to connect")
                val socket = Socket(address, MA_PORT)
                isRunning = true

                writerStream = PrintWriter(BufferedWriter(OutputStreamWriter(socket.getOutputStream())))
                readerStream = BufferedReader(InputStreamReader(socket.getInputStream()))

                sendMessage("hello! im connected")

                Log.d("socket", "connected")

                Log.d("socket", "listen to commands...")

                while (isRunning) {
                    if (writerStream!!.checkError()) {
                        isRunning = false
                    }
                    val command = readerStream!!.readLine()
                    if (command != null && command.isNotBlank()) {
                        Log.d("socket", command)
                        when (command) {
                            IN_MESSAGE -> {
                                listener?.showMessage(readerStream!!.readLine())
                            }
                            IN_ADD_CLIENT -> {
                                listener?.addClient(readerStream!!.readLine())
                            }
                            IN_LIST_OF_CLIENTS -> {
                                listener?.setListOfClient(readerStream!!.readLine())
                            }
                            IN_REMOVE_CLIENT -> {
                                listener?.removeClient(readerStream!!.readLine())
                            }
                        }
                    }
                }

                Log.d("socket", "disconnected")
                writerStream = null
                readerStream = null
                listener = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendMessage(message: String) {
        if (writerStream != null && !writerStream!!.checkError()) {
            writerStream!!.println(OUT_MESSAGE)
            writerStream!!.println(message)
            writerStream!!.flush()
        }
    }

    fun setSocketListener(socketListener: SocketListener) {
        listener = socketListener
    }

    fun onDestroy() {
        writerStream!!.println(OUT_EXIT)
        writerStream!!.flush()
        isRunning = false
    }

    interface SocketListener {
        fun showMessage(message: String)
        fun addClient(id: String)
        fun setListOfClient(listOfClient: String)
        fun removeClient(id: String)
    }
}