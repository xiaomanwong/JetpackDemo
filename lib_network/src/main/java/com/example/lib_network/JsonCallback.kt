package com.example.lib_network

abstract class JsonCallback<T>() {


    abstract fun onSuccess(response: ApiResponse<T>)

    fun onError(apiResponse: ApiResponse<T>) {

    }

    fun onCacheSuccess(apiResponse: ApiResponse<T>) {

    }
}