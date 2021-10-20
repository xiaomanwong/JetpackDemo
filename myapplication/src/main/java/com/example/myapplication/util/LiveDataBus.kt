package com.example.myapplication.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

object LiveDataBus {

    private val busMap: HashMap<String, BusMutableLiveData<Any>> = HashMap()


    fun <T> with(key: String, cls: Class<T>): BusMutableLiveData<Any> {
        if (!busMap.containsKey(key)) {
            busMap[key] = BusMutableLiveData()
        }
        return busMap[key]!!
    }

}

class BusMutableLiveData<T> : MutableLiveData<T>() {


    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, observer)
        hook(observer)
    }

    private fun hook(observer: Observer<in T>) {
        try {
            // observer 存储在 mObservers （SafeIterableMap）中
            // SafeIterableMap<Observer<? super T>, ObserverWrapper> mObservers
            val liveDataClass = LiveData::class.java
            val mObserverField = liveDataClass.getDeclaredField("mObservers")
            mObserverField.isAccessible = true
            // 获取成员变量对象 map
            val mObserverObject = mObserverField.get(this)
            val mObserverClass = mObserverObject.javaClass
            val getMethod = mObserverClass.getDeclaredMethod("get", Any::class.java)
            getMethod.isAccessible = true
            val valueEntry = getMethod.invoke(mObserverObject, observer)
            var observerWrapper: Any? = null
            if (valueEntry != null && valueEntry is Map.Entry<*, *>) {
                observerWrapper = valueEntry.value
            }
            if (observerWrapper == null) {
                throw NullPointerException("observer wrapper is null")
            }
            // 避免泛型擦除
            val superclass: Class<in Any> = observerWrapper.javaClass.superclass as Class<in Any>
            // 获取mLastVersion
            val mLastVersionField = superclass.getDeclaredField("mLastVersion")
            mLastVersionField.isAccessible = true
            // 获取mVersion
            val mVersionFiled = liveDataClass.getDeclaredField("mVersion")
            mVersionFiled.isAccessible = true
            val mVersion = mVersionFiled.get(this)
            // 赋值
            mLastVersionField.set(observerWrapper, mVersion)

        } catch (
            e: Exception
        ) {
            e.printStackTrace()
        }

    }
}