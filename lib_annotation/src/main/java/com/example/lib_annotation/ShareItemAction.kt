package com.example.lib_annotation

import kotlin.reflect.KClass

/**
 * @author Quinn
 * @date 2023/7/21
 * @Desc
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ShareItemAction(
    val item: String,
    val first: Array<KClass<out Any>>,
    val second: Array<KClass<out Any>>
)
