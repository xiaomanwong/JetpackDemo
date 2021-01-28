package com.example.lib_network

import java.lang.reflect.Type

/**
 * @author wangxu
 * @date 20-5-19
 * @Description
 *
 */
interface Convert<T> {

    fun convert(response: String, type: Type): T?
    fun convert(response: String, clazz: Class<*>): Class<*>?
}