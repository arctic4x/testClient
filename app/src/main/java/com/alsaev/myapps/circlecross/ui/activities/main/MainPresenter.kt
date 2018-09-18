package com.alsaev.myapps.circlecross.ui.activities.main

class MainPresenter(val vview: MainContract.Vview) : MainContract.Presenter {

    private var socketClient: SocketClient? = null

    override fun init() {
        socketClient = SocketClient()
        socketClient!!.setSocketListener(object : SocketClient.SocketListener {
            override fun showMessage(message: String) {
                vview.showMessage(message)
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