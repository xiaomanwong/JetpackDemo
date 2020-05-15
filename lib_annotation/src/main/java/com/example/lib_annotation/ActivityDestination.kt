package com.example.lib_annotation

/**
 * @author wangxu
 * @date 20-5-11
 * @Description
 *
 */
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
annotation class ActivityDestination(
    val pageUrl: String,
    val needLogin: Boolean = false,
    val asStart: Boolean = false

)