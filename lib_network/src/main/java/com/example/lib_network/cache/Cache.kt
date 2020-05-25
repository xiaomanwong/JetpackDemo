package com.example.lib_network.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * @author wangxu
 * @date 20-5-20
 * @Description
 *
 */
@Entity(tableName = "cache_data")
class Cache : Serializable {
    //PrimaryKey 必须要有,且不为空,autoGenerate 主键的值是否由Room自动生成,默认false
    @PrimaryKey(autoGenerate = false)
    var key: String? = null

    @ColumnInfo(name = "_data")// 指定该字段在表中的列的名字
    var data: ByteArray? = null //@Embedded 对象嵌套,ForeignTable对象中所有字段 也都会被映射到cache表中,
}