package com.example.lib_network.cache

import java.io.*

/**
 * @author wangxu
 * @date 20-5-20
 * @Description
 *
 */

object CacheManager {

    fun getCache(key: String): Any? {
        val cache = CacheDatabase.database.getCache().query(key)

        if (cache.data != null) {
            return toObject(cache.data!!)
        }
        return null
    }

    private fun toObject(data: ByteArray): Any? {
        var bis: ByteArrayInputStream? = null
        var ois: ObjectInputStream? = null

        try {
            bis = ByteArrayInputStream(data)
            ois = ObjectInputStream(bis)
            return ois.readObject()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bis?.close()
                ois?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun <T> save(key: String?, body: T) {
        val cache = Cache()
        cache.key = key
        cache.data = toByteArray(body)
        CacheDatabase.database.getCache().save(cache)
    }

    private fun <T> toByteArray(body: T): ByteArray {
        var byteArrayOutputStream: ByteArrayOutputStream? = null
        var objectOutputStream: ObjectOutputStream? = null

        try {
            byteArrayOutputStream = ByteArrayOutputStream()
            objectOutputStream = ObjectOutputStream(byteArrayOutputStream)

            objectOutputStream.writeObject(body)
            objectOutputStream.flush()
            return byteArrayOutputStream.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            byteArrayOutputStream?.close()
            objectOutputStream?.close()
        }
        return byteArrayOf(0)
    }
}
