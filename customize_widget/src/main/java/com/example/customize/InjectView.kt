package com.example.customize

import androidx.annotation.IdRes

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class InjectView(@IdRes val id: Int)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class OnClick(@IdRes val id: IntArray)