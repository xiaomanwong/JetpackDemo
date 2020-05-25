package com.example.lib_network.cache

import androidx.room.*

/**
 * @author wangxu
 * @date 20-5-20
 * @Description
 *
 */
@Dao
interface CacheDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun save(cache: Cache):Long

    @Query("select * from cache_data where `key` = :key")
    fun query(key: String): Cache

    @Delete
    fun delete(key: String): Int

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(cache: Cache) : Int

}