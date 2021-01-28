package com.example.customize

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.customize.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val viewModel: MainViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(MainViewModel::class.java)
        binding.click = ProxyClick()
        binding.lifecycleOwner = this
        binding.vm = viewModel
    }


    inner class ProxyClick {

        fun addView(index: Int) {
            val textView = TextView(this@MainActivity)
            textView.setTextColor(Color.parseColor("#FFFFFF"))
            textView.text = "测试代码"
            when (index) {
                1 -> {
                    binding.llPrepare.addView(textView)
                }
                2 -> {
                    binding.llDoing.addView(textView)
                }
                3 -> {
                    binding.llDone.addView(textView)
                }
                4 -> {
                    binding.llReview.addView(textView)
                }
            }
        }
    }

}

