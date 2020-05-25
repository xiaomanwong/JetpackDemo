package com.example.lib_network

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import java.lang.reflect.Type

/**
 * @author wangxu
 * @date 20-5-19
 * @Description
 *
 */
class JsonConvert : Convert<JSONObject?> {
    override fun convert(response: String, type: Type): JSONObject? {
        val jsonObject = JSON.parseObject(response)
        val data = jsonObject.getJSONObject("data")
        if (data != null) {
            val data1 = data["data"]
            return JSONObject.parseObject(data1.toString(), type)
        }
        return null
    }

    override fun convert(response: String, clazz: Class<*>): Class<*>? {
        val jsonObject = JSON.parseObject(response)
        val data = jsonObject.getJSONObject("data")
        if (data != null) {
            val data1 = data["data"]
            return JSONObject.parseObject(data1.toString(), clazz) as Class<*>?
        }
        return null
    }
}