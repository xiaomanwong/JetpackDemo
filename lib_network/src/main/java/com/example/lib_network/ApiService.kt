package com.example.lib_network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class ApiService {


    companion object {
        var okHttpClient: OkHttpClient? = null
        var mBaseUrl: String? = null
        var mConvert: Convert<Any?>? = null
    }
    // ok http 的初始化
    init {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        okHttpClient = OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
        //http 证书问题

        val trustManagers = arrayOf<TrustManager>(
            object : X509TrustManager {
                override fun checkClientTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                    TODO("Not yet implemented")
                }

                override fun checkServerTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                    TODO("Not yet implemented")
                }

                override fun getAcceptedIssuers(): Array<X509Certificate?> {

                    return arrayOfNulls(0)
                }

            }
        )

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustManagers, SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)
        HttpsURLConnection.setDefaultHostnameVerifier { _, _ -> true }
    }


    open fun init(baseUrl: String, convert: Convert<Any?>): Unit {
        mBaseUrl = baseUrl
        mConvert = convert
    }

}