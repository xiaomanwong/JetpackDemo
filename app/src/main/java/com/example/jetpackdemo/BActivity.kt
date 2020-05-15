package com.example.jetpackdemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

/**
 * @author wangxu
 * @date 20-5-7
 * @Description
 *
 */
class BActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
}