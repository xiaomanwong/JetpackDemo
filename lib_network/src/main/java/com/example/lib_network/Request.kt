package com.example.lib_network

import androidx.annotation.IntDef
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.util.*


abstract class Request<T, R : Request<T, R>?>(url: String) {
    protected var mUrl: String = url

    private var cacheKey: String? = null
    private val headers = hashMapOf<String, String>()
    protected val params = hashMapOf<String, Objects>()


    companion object {
        // 仅仅只访问本地缓存，即便本地不存在，也不会发起网络请求
        const val CACHE_ONLY: Int = 1

        // 先访问缓存，同时发起网络请求，成功后缓存到本地
        const val CACHE_FIRST: Int = 2

        // 仅仅只访问服务器，不存储任何内容
        const val NET_ONLY: Int = 3

        // 先访问网络，成功后缓存到本地
        const val NET_CACHE: Int = 4
    }

    @IntDef(value = [CACHE_ONLY, CACHE_FIRST, NET_CACHE, NET_ONLY])
    annotation class CacheStrategy {

    }


    fun addHeader(key: String, value: String): Request<T, R> {
        headers[key] = value
        return this
    }

    fun addParam(key: String, value: Objects): Request<T, R> {
        val field = value.javaClass.getField("TYPE")
        val clazz = field.get(null) as Class<*>
        if (clazz.isPrimitive) {
            params[key] = value
        }
        return this
    }

    fun cacheKey(key: String): Request<T, R> {
        this.cacheKey = key
        return this
    }


    // 异步
    fun execute(callback: JsonCallback<T>): Unit {
        getCall().enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val response = ApiResponse<T>()
                response.message = e.message
                callback.onError(response)
            }

            override fun onResponse(call: Call, response: Response) {
                val apiResponse = parseResponse(response)

                apiResponse.success

                callback.onSuccess(apiResponse)

            }
        })
    }

    fun parseResponse(response: Response, callback: JsonCallback<T>): ApiResponse<T> {
        val apiResponse = ApiResponse<T>()
        apiResponse.message = response.message
        apiResponse.status = response.code
        apiResponse.success = response.isSuccessful

        if (apiResponse.success!!) {
            val content: String = response.body.toString()
            if (callback != null) {
                val genericSuperclass: ParameterizedType =
                    callback.javaClass.genericSuperclass as ParameterizedType


            }
        }
    }

    // 同步
    fun execute(): Unit {

    }

    private fun getCall(): Call {
        val builder = okhttp3.Request.Builder()
        addHeaders(builder)
        val request = generateRequest(builder)
        val call = ApiService.okHttpClient?.newCall(request = request)
        return call!!
    }

    abstract fun generateRequest(builder: okhttp3.Request.Builder): okhttp3.Request

    private fun addHeaders(builder: okhttp3.Request.Builder) {
        for (entry in headers.entries) {
            builder.addHeader(entry.key, entry.value)
        }
    }

}