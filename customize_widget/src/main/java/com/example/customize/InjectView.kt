package com.example.customize

import androidx.annotation.IdRes
import androidx.annotation.IntDef

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class InjectView(@IdRes val id: Int)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@IntDef(OnClick.a)
annotation class OnClick(@IdRes val id: IntArray){
    companion object{
        const val a = 1;
    }
}