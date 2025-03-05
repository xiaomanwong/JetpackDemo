package com.example.lib.factory

import java.util.Arrays

/**
 * @author Quinn
 * @date 2024/5/30
 * @Desc
 */


fun main() {
    getRecentlyChatList({
        print(Arrays.toString(it.toTypedArray()))
    }, listOf(1, 2))


}

fun getRecentlyChatList(block: (List<String>) -> Unit, t: List<Int>?) {
    val lastFriendList = mutableListOf<Int>()
   block.invoke( t?.filter { (it % 2) == 0 }?.take(5)?.map { "$it" } ?: emptyList<String>())
//    kotlin.run {
//        t?.forEach {
//            if (lastFriendList.size == 3) {
//                println("getRecentlyChatList: ${lastFriendList.size}")
//                return
//            }
//            lastFriendList.add(it)
//
//        }
//    }
//    block.invoke(lastFriendList)
}