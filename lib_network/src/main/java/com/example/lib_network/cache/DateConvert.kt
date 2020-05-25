package com.example.lib_network.cache

import androidx.room.TypeConverter
import java.util.*

/**
 * @author wangxu
 * @date 20-5-20
 * @Description
 *
 */
class DateConvert {

    @TypeConverter
    fun Long2Date(date: Long): Date {
        return Date(date)
    }

    @TypeConverter
    fun date2Long(date: Date): Long {
        return date.time
    }
}