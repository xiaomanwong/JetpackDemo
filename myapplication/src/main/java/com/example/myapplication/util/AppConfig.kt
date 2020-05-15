package com.example.myapplication.util

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.example.myapplication.mode.Destination
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * @author wangxu
 * @date 20-5-12
 * @Description
 *
 */

var mDestination: HashMap<String, Destination>? = null

fun getDestConfig(): HashMap<String, Destination> {
    if (mDestination == null) {
        val content = parseFile("destination.json")
        mDestination = JSON.parseObject(
            content,
            object : TypeReference<HashMap<String, Destination>>() {}.type
        )
    }
    return mDestination!!
}

fun parseFile(fileName: String): String {
    val assets = getApplication().resources.assets
    val inputStream = assets.open(fileName)
    val bufferReader = BufferedReader(InputStreamReader(inputStream))
    var line: String
    val buffer = StringBuffer()
    line = bufferReader.readLine()
    while (line != null) {
        buffer.append(line)
        line = bufferReader.readLine()
    }

    bufferReader.close()
    inputStream.close()
    return buffer.toString()
}
