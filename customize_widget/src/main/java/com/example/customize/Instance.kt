package com.example.customize

enum class Instance(val parameterName: String) : Methods {
    A("sss") {
        override fun execture() {
            println("我是 A 枚举 $parameterName")
        }
    },
    B("b") {
        override fun execture() {
            println("我是 B 枚举")
        }
    };
}

fun main() {
    Instance.valueOf("A").execture()
}
interface Methods {
    fun execture()
}