package com.example.customize

import android.app.Activity
import android.view.View

object InjectUtil {


    fun injectView(activity: Activity) {
        val javaClass = activity.javaClass
        // 获取所有属性并执行 findViewById() 方法赋值
        val declaredFields = javaClass.declaredFields
        declaredFields.forEach { field ->
            val declaredAnnotations = field.declaredAnnotations
            declaredAnnotations.forEach { annotation ->
                if (annotation is InjectView) {
                    val view = activity.findViewById<View>(annotation.id)
                    field.isAccessible = true
                    field.set(activity, view)
                }
            }
        }
        // 获取所有方法，设置点击事件
        val declaredMethods = javaClass.declaredMethods
        declaredMethods.forEach { method ->
            val declaredAnnotations = method.declaredAnnotations
            declaredAnnotations.forEach { annotation ->
                if (annotation is OnClick) {
                    annotation.id.forEach { id ->
                        activity.findViewById<View>(id).setOnClickListener {
                            method.invoke(activity, it)
                        }
                    }
                    return@forEach
                }
            }
        }
    }
}