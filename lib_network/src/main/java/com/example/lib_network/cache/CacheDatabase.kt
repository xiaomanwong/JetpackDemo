package com.example.lib_network.cache

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lib_common.getApplication

/**
 * @author wangxu
 * @date 20-5-20
 * @Description 数据读取，存储的转换器，
 *
 */
@Database(entities = [Cache::class], version = 1)
abstract class CacheDatabase : RoomDatabase() {
    companion object {
        val database: CacheDatabase =
            Room.databaseBuilder(getApplication(), CacheDatabase::class.java, "jetpack_demo")
                .setJournalMode(JournalMode.AUTOMATIC)
                .allowMainThreadQueries()
                .build()

    }

    abstract fun getCache(): CacheDao
}