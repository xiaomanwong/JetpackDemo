package com.example.lib_network

import okhttp3.FormBody

class PostRequest<T>(mUrl: String) : Request<T, PostRequest<T>>(mUrl) {
    override fun generateRequest(builder: okhttp3.Request.Builder): okhttp3.Request {
        val formBody = FormBody.Builder()
        for (entry in params.entries) {
            formBody.add(entry.key, entry.value.toString())
        }
        return builder.url(mUrl).post(formBody.build()).build()
    }

}