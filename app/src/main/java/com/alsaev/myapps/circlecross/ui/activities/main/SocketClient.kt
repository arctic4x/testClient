package com.alsaev.myapps.circlecross.ui.activities.main

import android.util.Log
import java.io.*
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket

const val MA_PORT = 6969

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
                    //if (command!=null && command.isNotBlank()){
                        Log.d("socket", "message ${command}")
                        listener?.showMessage(command)
                    //}
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
            writerStream!!.println(message)
            writerStream!!.flush()
        }
    }

    fun setSocketListener(socketListener: SocketListener){
        listener = socketListener
    }

    fun onDestroy() {
        sendMessage("EXIT")
        isRunning = false
    }

    interface SocketListener {
        fun showMessage(message: String)
    }
}