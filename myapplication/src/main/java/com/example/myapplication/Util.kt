package com.example.myapplication

import androidx.lifecycle.MutableLiveData

/**
 * @author Quinn
 * @date 2023/6/14
 * @Desc
 */
object Util {


    val stringLive: MutableLiveData<String> = MutableLiveData()
    val listLiveData = MutableLiveData<List<Student>>()
}

class Student(val name: String, var age: Int) {
    override fun toString(): String {
        return "[name: $name, age:$age]"
    }
}