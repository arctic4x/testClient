package com.alsaev.myapps.circlecross.ui.activities.main

class MainPresenter(val vview: MainContract.Vview) : MainContract.Presenter {

    private var socketClient: SocketClient? = null

    override fun init() {
        socketClient = SocketClient()
        socketClient!!.setSocketListener(object : SocketClient.SocketListener {
            override fun showMessage(message: String) {
                vview.showMessage(message)
            }

            override fun addClient(id: String) {
                try {
                    vview.addClient(Integer.parseInt(id))
                } catch (e: NumberFormatException) {
                }
            }

            override fun setListOfClient(listOfClient: String) {
                val list = listOfClient.trim().split(" ")
                vview.addListOfClient(list)
            }

            override fun removeClient(id: String) {
                try {
                    vview.removeClient(Integer.parseInt(id))
                }catch (e: NumberFormatException){}
            }
        })
        socketClient!!.start()
    }

    override fun onDestroy() {
        socketClient?.onDestroy()
    }

    override fun submitMessage(message: String) {
        socketClient?.sendMessage(message)
    }
}