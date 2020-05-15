package com.example.myapplication.util

import android.app.Application

/**
 * @author wangxu
 * @date 20-5-12
 * @Description
 *
 */


lateinit var mApplication: Application
fun getApplication(): Application {
    val method =
        Class.forName("android.app.ActivityThread").getDeclaredMethod("currentApplication")
    mApplication = method.invoke(null, null) as Application
    return mApplication
}
