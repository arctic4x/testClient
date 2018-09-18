package com.alsaev.myapps.circlecross.ui.activities.main

interface MainContract {
    interface Vview {
        fun showMessage(message: String)

    }

    interface Presenter {
        fun init()
        fun onDestroy()
        fun submitMessage(message: String)
    }
}