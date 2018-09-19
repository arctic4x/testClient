package com.alsaev.myapps.circlecross.ui.activities.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.RadioButton
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
            val checkedBtnId = clients_container.checkedRadioButtonId
            val checkedBtn = findViewById<RadioButton>(checkedBtnId)
            if (checkedBtn.tag != null && checkedBtn.tag.toString().isNotBlank()) {
                presenter.submitMessage(et_text.text.toString(), checkedBtn.tag.toString())
            } else
                presenter.submitMessage(et_text.text.toString())
            et_text.text.clear()
        }

        clients_container.setOnCheckedChangeListener { group, checkedId ->
            findViewById<RadioButton>(checkedId)
        }
    }

    override fun showMessage(message: String) {
        runOnUiThread { tv_text.text = message }
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun addClient(id: Int) {
        runOnUiThread {
            val btn = RadioButton(this)
            btn.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            btn.text = id.toString()
            btn.tag = id.toString()
            clients_container.addView(btn)
        }
    }

    override fun removeClient(id: Int) {
        runOnUiThread {
            val view = clients_container.findViewWithTag<RadioButton>(id.toString())
            if (view != null) clients_container.removeView(view)
        }
    }

    override fun addListOfClient(list: List<String>) {
        runOnUiThread {
            list.forEach {
                val btn = RadioButton(this)
                btn.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                btn.text = it
                btn.tag = it
                clients_container.addView(btn)
            }
        }
    }
}
