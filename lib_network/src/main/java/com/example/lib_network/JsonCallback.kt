package com.example.lib_network

class JsonCallback<T> {


    fun onSuccess(response: ApiResponse<T>): Unit {

    }

    fun onError(apiResponse: ApiResponse<T>) {

    }

    fun onCacheSuccess(apiResponse: ApiResponse<T>) {

    }
}