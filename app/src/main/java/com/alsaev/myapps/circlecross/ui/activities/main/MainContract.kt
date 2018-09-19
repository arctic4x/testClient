package com.alsaev.myapps.circlecross.ui.activities.main

interface MainContract {
    interface Vview {
        fun showMessage(message: String)
        fun addClient(id: Int)
        fun removeClient(id: Int)
        fun addListOfClient(list: List<String>)

    }

    interface Presenter {
        fun init()
        fun onDestroy()
        fun submitMessage(message: String)
        fun submitMessage(message: String, clientId: String)
    }
}