package com.example.jetpackdemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startNextActivity(view: View) {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putString("haha", "s")
        intent.setClass(this, BActivity::class.java)
        intent.putExtras(bundle)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("收到了结果${resultCode == RESULT_OK}")
        Toast.makeText(this, "hahaha收到了结果${resultCode == RESULT_OK}", Toast.LENGTH_LONG).show()
    }
}

