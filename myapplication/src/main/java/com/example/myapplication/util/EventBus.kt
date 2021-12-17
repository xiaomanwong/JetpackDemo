package com.example.myapplication.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

/**
 * @author admin
 * @date 2021/11/19
 * @Desc
 */

var map: MutableMap<String, List<Channel<Any>>> = mutableMapOf()


inline fun <reified T> T.post() {
    if (!map.containsKey(T::class.java.name)) {
        map[T::class.java.name] = listOf(Channel())
    }
    CoroutineScope(context = EmptyCoroutineContext).launch {

        map[T::class.java.name]?.forEach {
            it.send(this@post as Any)
        }
    }
}

inline fun <T, reified R> T.onEvent(noinline action: suspend (R) -> Unit) {
    if (!map.containsKey(R::class.java.name)) {
        map[R::class.java.name] = listOf(Channel())
    }
    CoroutineScope(context = EmptyCoroutineContext).launch(Dispatchers.Main) {
        map[R::class.java.name]?.forEach {
            CoroutineScope(context = EmptyCoroutineContext).launch {
                action.invoke(it.receive() as R)
            }
        }

    }
}