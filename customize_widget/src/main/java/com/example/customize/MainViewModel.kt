package com.example.customize

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val textValue: MutableLiveData<String> = MutableLiveData()
    val mContext: Context = application
    var index: Int = 0

    fun startGesturePassword(): Unit {
        println("startGesturePassword${++index}")
        textValue.value = (++index).toString()
    }
}