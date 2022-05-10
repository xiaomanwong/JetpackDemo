package com.example.myapplication.media.player

import com.example.myapplication.App
import com.example.myapplication.media.TruelyAudioFileManager
import com.example.myapplication.media.TruelyAudioStatusCode
import com.yidian.local.service.audio.model.AudioModel
import com.yidian.local.service.audio.model.TruelyAudioElement
import org.json.JSONArray
import org.json.JSONObject

/**
 * @author admin
 * @date 2022/5/10
 * @Desc
 */
open class BaseDownloadAndPlayPlayer : OfflineBaseAudioPlayer() {


    var startTime: Long = 0


    fun obtainJudeBoxSource(
        sourceList: JSONArray
    ): ArrayList<AudioModel> {
        // 数据正常后，开始解析数据
        val sources: ArrayList<AudioModel> = arrayListOf()
        repeat(sourceList.length()) {
            val element = sourceList.getJSONObject(it)
            sources.add(
                AudioModel(
                    element.optInt("music_id"),
                    element.optString("music_url"),
                    element.optString("name"),
                    element.optString("signer"),
                    element.optString("music_category"),
                    element.optInt("sort"),
                    element.optInt("duration"),
                    element.optString("create_time"),
                    element.optString("update_time")
                )
            )
        }
        return sources
    }


    fun obtainGuitarAndPollySource(
        element: JSONObject
    ): AudioModel {
        return AudioModel(
            element.optInt("music_id"),
            element.optString("music_url"),
            element.optString("name"),
            element.optString("signer"),
            element.optString("music_category"),
            element.optInt("sort"),
            element.optInt("duration"),
            element.optString("create_time"),
            element.optString("update_time")
        )
    }


    /**
     * 将数据包装成下载需要的数据
     */
    fun swapDownloadElement(
        sourceList: ArrayList<AudioModel>,
        index: Int,
        downloadAll: Boolean = false
    ): ArrayList<TruelyAudioElement> {
        // 调整数据源顺序
        val tempNewSourceList: ArrayList<AudioModel> = arrayListOf()
        tempNewSourceList.addAll(sourceList.subList(index, sourceList.size))
        // 转换数据位置，如果需要列表循环播放，那么放开对应的位置
        if (downloadAll) {
            tempNewSourceList.addAll(sourceList.subList(0, index))
        }

        val downloadElementList = ArrayList<TruelyAudioElement>()
        tempNewSourceList.forEach {
            checkLocal(downloadElementList, it)
        }
        return downloadElementList
    }


    /**
     * 将数据包装成下载需要的数据
     */
    fun wrapDownloadElement(
        element: AudioModel
    ): ArrayList<TruelyAudioElement> {
        // 调整数据源顺序

        val downloadElementList = ArrayList<TruelyAudioElement>()
        checkLocal(downloadElementList, element)
        return downloadElementList
    }

    /**
     * 检查本地是否有文件，没有则不添加到下载队列中
     */
    private fun checkLocal(downloadList: ArrayList<TruelyAudioElement>, element: AudioModel) {
        val localPath = TruelyAudioFileManager.getLocalFilePath(
            App.instance,
            element.music_url
        )
//        if (!TruelyAudioFileManager.checkLocalCache(localPath)) {
            // 准备去播放
            downloadList.add(
                TruelyAudioElement(
                    this,
                    element.music_url,
                    localPath,
                    element = element
                )
            )
//        }
    }

    /**
     * 下载完成，开始播放（只有第一个下载完成，才播放，其余下载完成不播放）
     */
    fun downloadCompleteAndPlay(element: TruelyAudioElement) {
        if (element.endTime == -1L) {
            // -1 表示任务下载失败
            callback.invoke(
                TruelyAudioStatusCode.DOWNLOAD_ERROR,
                TruelyAudioStatusCode.STR_DOWNLOAD_ERROR,
                null
            )
        }
        startTime += (element.endTime - element.startTime)
        play(element.localPath)
    }

    override fun onStatusChanged(lapt: TruelyAudioPlayer?, status: Int, other: Any?) {
        super.onStatusChanged(lapt, status, other)
        if (status == TruelyAudioPlayer.Status.STATUS_READY) {
            // ready
            val duration: Int = other as Int
            // 如果当前总时长大于起播时间，释放，并抛出异常，需要重新同步
            if (duration <= startTime.toInt()) {
                callback.invoke(
                    TruelyAudioStatusCode.ALARM_SYNC,
                    TruelyAudioStatusCode.STR_ALARM_SYNC,
                    null
                )
                return
            }
            // 跳转到起播位置
            player.seek(startTime.toInt())
        }
    }
}