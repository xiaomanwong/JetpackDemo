package com.example.lib_network

class ApiResponse<T> {
    var success: Boolean? = null
    var status: Int? = null
    var message: String? = null
    var body: T? = null
}