package com.example.myapplication.util

import android.app.Application

/**
 * @author wangxu
 * @date 20-5-12
 * @Description
 *
 */


var mApplication: Application? = null
fun getApplication(): Application {
    if (mApplication == null) {
        val method =
            Class.forName("android.app.ActivityThread").getMethod("currentApplication")
        mApplication = method.invoke(null) as Application
    }
    return mApplication!!
}
