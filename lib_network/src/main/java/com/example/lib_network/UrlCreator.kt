package com.example.lib_network

import java.net.URLEncoder
import java.util.*


object UrlCreator {


    fun createUrlFromParams(url: String, param: HashMap<String, Objects>): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append(url)
        if (url.indexOf("?") > 0 || url.indexOf("&") > 0) {
            stringBuilder.append("&")
        } else {
            stringBuilder.append("?")
        }
        for (entry in param.entries) {
            val value = URLEncoder.encode(entry.value.toString(), "UTF-8")
            stringBuilder.append(entry.key).append("=").append(entry.value)
        }
        stringBuilder.deleteCharAt(stringBuilder.length - 1)
        return stringBuilder.toString()
    }
}