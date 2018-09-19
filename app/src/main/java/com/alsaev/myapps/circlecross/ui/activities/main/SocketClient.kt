package com.alsaev.myapps.circlecross.ui.activities.main

import android.util.Log
import java.io.*
import java.net.InetAddress
import java.net.Socket

const val MA_PORT = 6969

private const val OUT_EXIT = "EXIT"
private const val OUT_MESSAGE = "MESSAGE"
private const val OUT_MESSAGE_TO_CLIENT = "MESSAGE_TO_CLIENT"

private const val IN_MESSAGE = "MESSAGE"
private const val IN_LIST_OF_CLIENTS = "LIST_OF_CLIENTS"
private const val IN_ADD_CLIENT = "ADD_CLIENT"
private const val IN_REMOVE_CLIENT = "REMOVE_CLIENT"
private const val IN_MESSAGE_FROM_CLIENT = "MESSAGE_FROM_CLIENT"

class SocketClient() : Thread() {
    private var isRunning = false

    private var writerStream: PrintWriter? = null
    private var readerStream: BufferedReader? = null
    private var listener: SocketListener? = null

    override fun run() {
        try {
            Log.d("socket", "try to find server")
            val address = InetAddress.getByName("192.168.0.112")

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
                                showMessage()
                            }
                            IN_ADD_CLIENT -> {
                                addClient()
                            }
                            IN_LIST_OF_CLIENTS -> {
                                setListOfClient()
                            }
                            IN_REMOVE_CLIENT -> {
                                removeClient()
                            }
                            IN_MESSAGE_FROM_CLIENT -> {
                                showMessageFromCliet()
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

    private fun showMessageFromCliet() {
        val clientId = readerStream!!.readLine()
        val message = readerStream!!.readLine()
        listener?.showMessageFromClient(clientId, message)

    }

    private fun removeClient() {
        val clientId = readerStream!!.readLine()
        listener?.removeClient(clientId)
    }

    private fun setListOfClient() {
        val listOfClient = readerStream!!.readLine()
        listener?.setListOfClient(listOfClient)
    }

    private fun addClient() {
        val clientId = readerStream!!.readLine()
        listener?.addClient(clientId)
    }

    private fun showMessage() {
        val message = readerStream!!.readLine()
        listener?.showMessage(message)
    }

    fun sendMessage(message: String, clientId: String) {
        Thread {
            if (writerStream != null && !writerStream!!.checkError()) {
                writerStream!!.println(OUT_MESSAGE_TO_CLIENT)
                writerStream!!.println(clientId)
                writerStream!!.println(message)
                writerStream!!.flush()
            }
        }.start()
    }

    fun sendMessage(message: String) {
        Thread {
            if (writerStream != null && !writerStream!!.checkError()) {
                writerStream!!.println(OUT_MESSAGE)
                writerStream!!.println(message)
                writerStream!!.flush()
            }
        }.start()
    }

    fun setSocketListener(socketListener: SocketListener) {
        listener = socketListener
    }

    fun onDestroy() {
        Thread {
            writerStream!!.println(OUT_EXIT)
            writerStream!!.flush()
            isRunning = false
        }.start()
    }

    interface SocketListener {
        fun showMessage(message: String)
        fun addClient(id: String)
        fun setListOfClient(listOfClient: String)
        fun removeClient(id: String)
        fun showMessageFromClient(clientId: String, message: String)
    }
}