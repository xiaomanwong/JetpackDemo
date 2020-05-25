package com.example.jetpackdemo

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

/**
 * @author wangxu
 * @date 20-5-7
 * @Description
 *
 */
class CActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startNextActivity(v: View) {
        setResult(Activity.RESULT_OK)
        finish()
    }
}