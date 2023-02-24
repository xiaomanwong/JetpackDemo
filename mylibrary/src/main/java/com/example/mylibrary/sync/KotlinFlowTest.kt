package com.example.mylibrary.sync

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

/**
 * @author Quinn
 * @date 2023/2/24
 * @Desc
 */


fun main() {

    runBlocking(Dispatchers.Default) {
//        listOf(1, 3, 12, 41, 5).asFlow().collect {
//            print("$it,")
//        }
//
//        flowOf(3423, 234, 234)

//        println()
        flow {
            for (i in 1..6) {
                //发射数据
                emit(i)
                println("emit:$i，thread name: ${Thread.currentThread().name}")
            }
        }.flowOn(Dispatchers.IO)
            .catch {
                //异常处理
                println("catch thread name: ${Thread.currentThread().name}")
            }.flowOn(Dispatchers.IO).onCompletion {
                //完成回调
                println("onCompletion thread name: ${Thread.currentThread().name}")
            }.collect { num ->
                // 具体的消费处理
                // 只有collect时才会生产数据，原因之后会讲
                println("collect:$num thread name: ${Thread.currentThread().name}")
            }
    }

}