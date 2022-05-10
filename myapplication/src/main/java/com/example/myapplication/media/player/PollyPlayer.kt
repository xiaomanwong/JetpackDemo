package com.example.myapplication.media.player

import com.example.myapplication.App
import com.example.myapplication.media.TruelyAudioFileManager
import com.example.myapplication.media.TruelyAudioStatusCode
import com.yidian.local.service.audio.download.TruelyAudioDownloadManager
import org.json.JSONObject

/**
 * @author admin
 * @date 2022/5/7
 * @Desc
 */
class PollyPlayer : BaseDownloadAndPlayPlayer() {

    override fun play(
        source: Any,
        category: String,
        callback: (code: Int, msg: String, other: Any?) -> Unit
    ) {
        super.play(source, category, callback)
        // 音乐盒需要获取列表
        val params = source as JSONObject
        val sourceJsonObject = params.optJSONObject("source")
        if (sourceJsonObject == null) {
            callback.invoke(
                TruelyAudioStatusCode.PARAM_ERROR,
                TruelyAudioStatusCode.STR_PARAM_ERROR,
                null
            )
            return
        }
        val obtainJudeBoxSource = obtainGuitarAndPollySource(sourceJsonObject)
        startTime = params.optLong("startTime")
        val srcPath = obtainJudeBoxSource.music_url
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
            } else {
                val downloadElement = wrapDownloadElement(obtainJudeBoxSource)
                TruelyAudioDownloadManager.startDownload(downloadElement, true)
            }
        } catch (e: Exception) {
            callback.invoke(
                TruelyAudioStatusCode.PARAM_ERROR,
                TruelyAudioStatusCode.STR_PARAM_ERROR,
                params
            )
        }
    }

    override fun onStatusChanged(lapt: TruelyAudioPlayer?, status: Int, other: Any?) {
        super.onStatusChanged(lapt, status, other)
        if (status == TruelyAudioPlayer.Status.STATUS_COMPLETE) {
            // 播放完成释放播放器
            callback.invoke(
                TruelyAudioStatusCode.PLAY_COMPLETE,
                TruelyAudioStatusCode.STR_PLAY_SINGLE_COMPLETE,
                null
            )
        }
    }
}