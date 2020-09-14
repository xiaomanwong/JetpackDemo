package com.example.customize

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InjectActivity : AppCompatActivity() {


    @InjectView(R.id.tv_name)
    lateinit var tv: TextView

    @InjectView(R.id.tv_name2)
    lateinit var tv2: TextView
    var count: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inject)
        InjectUtil.injectView(this)

        tv.text = "我是注入进来的"
        tv2.text = "我是注入进来的"
        Instance.valueOf("A").execture()

    }


    @OnClick([R.id.tv_name, R.id.tv_name2])
    fun clickListener(view: View) {
        when (view.id) {
            R.id.tv_name -> {
                tv.text = "我是注入进来的 textview1 : ${++count}"
            }
            R.id.tv_name2 -> {
                tv2.text = "我是注入进来的 textview2 : ${++count}"
                startActivity(Intent(this, BActivity::class.java))
                LiveDataBus.with("abc", String.javaClass).value = "hhahaha"
            }
            else -> {
            }
        }

    }
}