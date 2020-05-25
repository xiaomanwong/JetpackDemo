package com.example.myapplication.util

import android.content.res.AssetManager
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.example.myapplication.mode.BottomBar
import com.example.myapplication.mode.Destination
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

/**
 * @author wangxu
 * @date 20-5-12
 * @Description
 *
 */

var mDestination: HashMap<String, Destination>? = null

var mBottomBar: BottomBar? = null

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

fun getBottomConfig(): BottomBar? {
    if (mBottomBar == null) {
        val content = parseFile("main_tabs_config.json")
        println("tabs: $content")
        mBottomBar = JSONObject.parseObject(content, BottomBar::class.java)
    }
    return mBottomBar
}

private fun parseFile(fileName: String): String {
    val assets: AssetManager = com.example.lib_common.getApplication().assets
    var `is`: InputStream? = null
    var br: BufferedReader? = null
    val builder = StringBuilder()
    try {
        `is` = assets.open(fileName)
        br = BufferedReader(InputStreamReader(`is`))
        var line: String?
        while (br.readLine().also { line = it } != null) {
            builder.append(line)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            `is`?.close()
            br?.close()
        } catch (e: Exception) {
        }
    }
    return builder.toString()
}
