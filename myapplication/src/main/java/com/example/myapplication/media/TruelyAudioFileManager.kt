package com.example.myapplication.media

import android.content.Context
import com.example.myapplication.App
import java.io.File

/**
 *
 *
 * 负责调度和下载
 *
 * @author admin
 * @date 2022/5/6
 * @Desc
 */
object TruelyAudioFileManager {


    /**
     * 检查本地是否有文件
     */
    fun checkLocalCache(path: String): Boolean {
        return if (path.startsWith("http")) {
            throw IllegalArgumentException("path must be local path , current path = $path ")
        } else {
            File(path).exists()
        }
    }

    /**
     * 获取文件路径
     * 将网络路径转换为本地缓存路径
     * 供下载和播放使用
     */
    fun getLocalFilePath(context: Context, path: String): String {
        val cachePath = context.cacheDir.absolutePath + "/audio"
        return if (path.startsWith("http")) {
            val fileName = path.substring(path.lastIndexOf("/"))
            cachePath + fileName
        } else {
            path
        }
    }

    fun checkCacheAndReturn(path:String, execWhenNotExist: ((String) -> Unit)?) : String {
        val localPath = getLocalFilePath(App.instance, path)
        return if (File(localPath).exists()) localPath else {
            execWhenNotExist?.let {
                it(localPath)
            }
            path
        }
    }


}