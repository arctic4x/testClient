package com.alsaev.myapps.circlecross.ui.activities.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.alsaev.myapps.circlecross.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainContract.Vview {

    private lateinit var presenter: MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainPresenter(this)
        presenter.init()

        btn_submit.setOnClickListener {
            presenter.submitMessage(et_text.text.toString())
            et_text.text.clear()
        }
    }

    override fun showMessage(message: String) {
        runOnUiThread { tv_text.text = message }
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
