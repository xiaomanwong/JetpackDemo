package com.example.customize

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.Toast

class MainActivity : Activity() {


    var handler1 = Handler(Looper.myLooper(),Handler.Callback {
        println("handler 1")
        true
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startNextActivity(view: View) {
        val message = Message()

        handler1.sendEmptyMessage(1)
//        handler1.post()
//        handler2.sendEmptyMessage(1)
//        handler3.sendEmptyMessage(1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("收到了结果${resultCode == RESULT_OK}")
        Toast.makeText(this, "hahaha收到了结果${resultCode == RESULT_OK}", Toast.LENGTH_LONG).show()
    }
}

