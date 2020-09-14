package com.example.customize

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

/**
 * @author wangxu
 * @date 20-5-7
 * @Description
 *
 */
class BActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LiveDataBus.with("abc", String::class.java).observe(this, Observer {
            Toast.makeText(this@BActivity, it.toString(), Toast.LENGTH_LONG).show()
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("ssssssssssssssssssssssssssssssssssss")
        setResult(Activity.RESULT_OK)
//        finish()
    }

    fun startNextActivity(view: View) {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putString("haha", "s")
        intent.putExtras(bundle)
        intent.setClass(this, CActivity::class.java)
        startActivity(intent)
    }

    override fun onPause() {
        super.onPause()
//        Thread.sleep(10_000)
    }
}