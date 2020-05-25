package com.example.myapplication.ui.publish

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lib_annotation.ActivityDestination
import com.example.myapplication.R

@ActivityDestination(pageUrl = "main/tabs/publish", asStart = false)
class PublishActivity : AppCompatActivity() {

    private lateinit var publishViewModel: PublishViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_dashboard)
    }
}
