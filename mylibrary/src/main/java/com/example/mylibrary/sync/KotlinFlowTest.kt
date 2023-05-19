package com.example.mylibrary.sync

import kotlinx.coroutines.*
import org.json.JSONObject
import kotlin.random.Random

//package com.example.mylibrary.sync
//
//import kotlinx.coroutines.*
//import kotlinx.coroutines.flow.*
//import kotlin.concurrent.thread
//
///**
// * @author Quinn
// * @date 2023/2/24
// * @Desc
// */
//


// 代码段6


fun Job.log(){
    logX(
        """
        isActive = $isActive
        isCancelled = $isCancelled
        isCompleted = $isCompleted
    """.trimIndent())

   println( JSONObject("{}").toString())
}

fun logX(any: Any?){
    println ("""
        ==================
        $any 
        Thread: ${Thread.currentThread().name}
        ==================
    """.trimIndent())
    println()
}
fun main() = runBlocking {
    JSONObject("fakse".toString())
    suspend fun download() {
        // 模拟下载任务
        val time = (Random.nextDouble() * 1000).toLong()
        logX("Delay time: = $time")
        delay(5000)
    }
    val job = launch(start = CoroutineStart.LAZY) {
        logX("Coroutine start!")
        download()
        logX("Coroutine end!")
    }
    delay(500L)
    job.log()
    job.start()
//    job.log()
    job.invokeOnCompletion {
        job.log() // 协程结束以后就会调用这里的代码

    }
//    job.join()      // 等待协程执行完毕
    logX("Process end!")
//    JSONObject("null")


}

//
//fun main1() = runBlocking {
////    val deferred: Deferred<String> = async {
////        println("In async:${Thread.currentThread().name}")
////        delay(1000L) // 模拟耗时操作
////        println("In async after delay!")
////        return@async "Task completed!"
////    }
//
//
//    withContext(Dispatchers.IO){
//        println("withContext IO")
//        delay(1000L)
//        println("withContext end")
//    }
//
////     不再调用
////     deferred.await()
//    println("xxxxxxxxxx")
////    delay(2000L)
//}
//
//fun main(){
//    GlobalScope.launch {
//
//
////     不再调用
////     deferred.await()
//        fun1()
//        println("xxxxxxxxxx")
//    }
//
//    println("xsdfasdfasdfasdf")
//    Thread.sleep(2000L)
//}
//
//private suspend fun fun1(){
//    withContext(Dispatchers.IO){
//        println("withContext IO")
//        delay(1000L)
//        withContext(Dispatchers.IO){
//            thread {
//                println("inner context thread")
//            }
//            println("inner context")
//        }
//        println("withContext end")
//    }
//}
//
////
////fun main() {
////
////    runBlocking(Dispatchers.Default) {
//////        listOf(1, 3, 12, 41, 5).asFlow().collect {
//////            print("$it,")
//////        }
//////
//////        flowOf(3423, 234, 234)
////
//////        println()
////        flow {
////            for (i in 1..6) {
////                //发射数据
////                emit(i)
////                println("emit:$i，thread name: ${Thread.currentThread().name}")
////            }
////        }.filter{
////            it > 2
////        }.flowOn(Dispatchers.IO)
////            .catch {
////                //异常处理
////                println("catch thread name: ${Thread.currentThread().name}")
////            }.flowOn(Dispatchers.IO).onCompletion {
////                //完成回调
////                println("onCompletion thread name: ${Thread.currentThread().name}")
////            }.collect { num ->
////                // 具体的消费处理
////                // 只有collect时才会生产数据，原因之后会讲
////                println("collect:$num thread name: ${Thread.currentThread().name}")
////            }
////    }
//
////
////    var x:Int? = null
////    x = 1
////    x?.plus(1)
////}
//
////fun plus1(x:Int?) = x?.plus(1)
////
////class Person(val name: String, var age: Int)
////
////data class Person2(val name:String, val age:Int)
////
////
////fun String?.lastElement():Char?{
////    return null
////}
////
////fun String.lastElement():Char?{
////    return null
////}