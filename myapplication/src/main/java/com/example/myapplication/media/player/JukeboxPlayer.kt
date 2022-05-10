package com.example.myapplication.media.player

import android.util.Log
import com.example.myapplication.App
import com.example.myapplication.media.TruelyAudioFileManager
import com.example.myapplication.media.TruelyAudioStatusCode
import com.yidian.local.service.audio.download.TruelyAudioDownloadManager
import com.yidian.local.service.audio.model.TruelyAudioElement
import org.json.JSONObject

/**
 * @author admin
 * @date 2022/5/6
 * @Desc
 */
class JukeboxPlayer : BaseDownloadAndPlayPlayer() {


    var index: Int = 0

    private lateinit var sourceList: ArrayList<TruelyAudioElement>
    override fun play(
        source: Any,
        category: String,
        callback: (code: Int, msg: String, other: Any?) -> Unit
    ) {
        super.play(source, category, callback)
        // 音乐盒需要获取列表
        val params = source as JSONObject
        val sourceJSONArray = params.optJSONArray("sourceList")
        if (sourceJSONArray == null) {
            callback.invoke(
                TruelyAudioStatusCode.PARAM_ERROR,
                TruelyAudioStatusCode.STR_PARAM_ERROR,
                null
            )
            return
        }
        val obtainJudeBoxSource = obtainJudeBoxSource(sourceJSONArray)
        startTime = params.optLong("startTime")
        index = params.optInt("index")
        val srcPath = obtainJudeBoxSource[index].music_url
        if (!srcPath.startsWith("http")) {
            callback.invoke(
                TruelyAudioStatusCode.PARAM_ERROR,
                TruelyAudioStatusCode.STR_PARAM_ERROR,
                null
            )
            return
        }
        val localPath = TruelyAudioFileManager.getLocalFilePath(App.instance, srcPath)

        try {
            if (TruelyAudioFileManager.checkLocalCache(localPath)) {
                // 准备去播放
                play(localPath)
            }
        } catch (e: Exception) {
            callback.invoke(
                TruelyAudioStatusCode.PARAM_ERROR,
                TruelyAudioStatusCode.STR_PARAM_ERROR,
                params
            )
            return
        }
        // 需要去下载， 顺序下载全部
        this.sourceList = swapDownloadElement(obtainJudeBoxSource, index + 1, true)
        TruelyAudioDownloadManager.startDownload(
            this.sourceList,
            index == params.optInt("index")
        )
    }


    override fun onStatusChanged(lapt: TruelyAudioPlayer?, status: Int, other: Any?) {
        super.onStatusChanged(lapt, status, other)

        // 循环播放
        if (status == TruelyAudioPlayer.Status.STATUS_COMPLETE) {
            // 播放完成一首歌，将继续播放列表
            if (++index >= sourceList.size) {
                // 列表播放完成
                index = 0
            }
            val localPath = sourceList[index].localPath
            try {
                if (TruelyAudioFileManager.checkLocalCache(localPath)) {
                    // 准备去播放
                    play(localPath)
                } else {
                    callback.invoke(
                        TruelyAudioStatusCode.DOWNLOAD_ERROR,
                        TruelyAudioStatusCode.STR_DOWNLOAD_ERROR,
                        null
                    )
                    return
                }
            } catch (e: Exception) {
                callback.invoke(
                    TruelyAudioStatusCode.PARAM_ERROR,
                    TruelyAudioStatusCode.STR_PARAM_ERROR,
                    null
                )
                return
            }
        }
        Log.d(
            "wangxu3",
            "JukeboxPlayer ===> onStatusChanged() called with: index = $index, status = $status, other = $other"
        )
    }
}