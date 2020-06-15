package com.example.lib_network

import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Log
import androidx.annotation.IntDef
import androidx.arch.core.executor.ArchTaskExecutor
import com.example.lib_network.cache.CacheManager
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.io.Serializable
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*


abstract class Request<T, R : Request<T, R>?>(url: String) : Cloneable {
    protected var mUrl: String = url
    private var mType: Type? = null
    private var mClazz: Class<*>? = null
    private var cacheKey: String? = null
    private var mCacheStrategy: Int = NET_ONLY
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


    fun setStrategy(cacheStrategy: Int): Request<T, R> {
        mCacheStrategy = cacheStrategy
        return this
    }

    fun cacheKey(key: String): Request<T, R> {
        this.cacheKey = key
        return this
    }


    fun readCache(): ApiResponse<T> {
        val key = if (TextUtils.isEmpty(cacheKey)) generateCacheKey() else cacheKey
        val cache = key?.let { CacheManager.getCache(it) }
        val result = ApiResponse<T>()
        result.status = 304
        result.body = cache as T
        result.message = "缓存读取成功"
        result.success = true
        return result
    }

    // 异步
    @SuppressLint("RestrictedApi")
    fun execute(callback: JsonCallback<T>) {
        if (mCacheStrategy != NET_ONLY) {
            ArchTaskExecutor.getIOThreadExecutor().execute {
                val response = readCache()
                callback.let {
                    callback.onSuccess(response)
                }
            }
        }

        if (mCacheStrategy != CACHE_ONLY) {
            getCall().enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    val response = ApiResponse<T>()
                    response.message = e.message
                    callback.onError(response)
                }

                override fun onResponse(call: Call, response: Response) {
                    val apiResponse = parseResponse(response, callback)
                    if (apiResponse.success!!) {
                        callback.onError(apiResponse)
                    } else {
                        callback.onSuccess(apiResponse)
                    }
                }
            })
        }
    }

    fun parseResponse(response: Response, callback: JsonCallback<T>?): ApiResponse<T> {
        val apiResponse = ApiResponse<T>()
        apiResponse.message = response.message
        apiResponse.status = response.code
        apiResponse.success = response.isSuccessful
        val convert = ApiService.mConvert
        try {
            val content: String = response.body.toString()
            if (apiResponse.success!!) {
                when {
                    callback != null -> {
                        val genericSuperclass: ParameterizedType =
                            callback.javaClass.genericSuperclass as ParameterizedType
                        val type = genericSuperclass.actualTypeArguments[0];
                        apiResponse.body = convert?.convert(content, type) as T
                    }
                    mType != null -> {
                        apiResponse.body = convert?.convert(content, mType!!) as T?
                    }
                    mClazz != null -> {
                        apiResponse.body = convert?.convert(content, mClazz!!) as T?
                    }
                    else -> {
                        Log.e("无法解析", "无法解析")
                    }
                }
            }
        } catch (e: IOException) {
            apiResponse.message = e.message
            apiResponse.status = 0
            apiResponse.success = false
            apiResponse.body = null
        }

        if (apiResponse.success!! && mCacheStrategy != NET_ONLY && apiResponse.body != null && apiResponse.body is Serializable) {
            saveCache(apiResponse.body)
        }
        return apiResponse
    }

    /**
     * 保存缓存数据
     */
    private fun <T> saveCache(result: T): Unit {
        val key = if (TextUtils.isEmpty(cacheKey)) generateCacheKey() else cacheKey
        CacheManager.save(key, result)
    }

    private fun generateCacheKey(): String {
        cacheKey = UrlCreator.createUrlFromParams(mUrl, params)
        return cacheKey!!
    }

    fun responseType(type: Type): R {
        mType = type
        return this as R
    }

    fun responseType(clazz: Class<*>): R {
        mClazz = clazz
        return this as R
    }

    // 同步
    fun execute(): ApiResponse<T> {

        if (mCacheStrategy == CACHE_ONLY) {
            return readCache()
        }

        var result: ApiResponse<T>? = null
        try {

            val response = getCall().execute()
            result = parseResponse(response, null)
        } catch (e: Exception) {
            if (result == null) {
                result = ApiResponse()
                result.success = false
                result.message = e.message

            }
        }

        return result!!
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