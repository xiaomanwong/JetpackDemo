package com.example.myapplication.media.player

import com.example.myapplication.App
import com.example.myapplication.media.ITruelyPlayer
import com.example.myapplication.media.TruelyAudioError
import com.example.myapplication.media.TruelyAudioFileManager
import com.example.myapplication.media.TruelyAudioPlayerManager
import com.example.myapplication.media.download.TruelyAudioDownloadManager
import com.example.myapplication.media.download.TruelyAudioElement

/**
 *
 * 规则播放
 *
 * @author admin
 * @date 2022/5/6
 * @Desc
 */

open class BaseAudioPlayer : ITruelyPlayer, TruelyAudioPlayer.OnStatusChangedListener {


    val player: TruelyAudioPlayer = TruelyAudioPlayer()
    var startTime: Int = 0
    var index = 0
    lateinit var callback: (code: Int, msg: String) -> Unit
    @TruelyAudioPlayerManager.AudioMediaCategory
    lateinit var category: String
    lateinit var sourceList: ArrayList<String>

    override fun play(
        sourceList: ArrayList<String>,
        index: Int,
        startTime: Long,
        category: String,
        callback: (code: Int, msg: String) -> Unit
    ) {

        this.sourceList = sourceList
        this.index = index
        this.category = category
        this.callback = callback
        // 处理文件缓存逻辑
        val srcPath = sourceList[index]
        val localPath = TruelyAudioFileManager.getLocalFilePath(App.instance, srcPath)
        val fileExits = TruelyAudioFileManager.checkLocalCache(localPath)
        if (fileExits) {
            // 准备去播放
            play(localPath)
            this.startTime = startTime.toInt()
        } else {
            // 需要去下载， 顺序下载全部
            val swapDownloadElement = swapDownloadElement(sourceList, index)
            TruelyAudioDownloadManager.startDownload(swapDownloadElement)
        }
    }

    /**
     * 下载完成，开始播放（只有第一个下载完成，才播放，其余下载完成不播放）
     */
    fun downloadCompleteAndPlay(element: TruelyAudioElement) {
        if (element.startTime == -1L) {
            // -1 表示任务下载失败
            callback.invoke(TruelyAudioError.DOWNLOAD_ERROR, TruelyAudioError.STR_DOWNLOAD_ERROR)
        }
        startTime = ((element.endTime - element.startTime)).toInt()
        play(element.localPath)
    }

    /**
     * 真正的开始播放
     */
    private fun play(localPath: String) {
        player.play(localPath)
        player.setStatusChangedListener(TruelyAudioPlayer.Status.STATUS_READY, this)
        player.setStatusChangedListener(TruelyAudioPlayer.Status.STATUS_COMPLETE, this)
        player.setStatusChangedListener(TruelyAudioPlayer.Status.STATUS_STOP, this)
        player.setStatusChangedListener(TruelyAudioPlayer.Status.STATUS_ERROR, this)
    }

    /**
     * 将数据包装成下载需要的数据
     */
    private fun swapDownloadElement(
        sourceList: ArrayList<String>,
        index: Int,
        downloadAll: Boolean = false
    ): ArrayList<TruelyAudioElement> {
        // 调整数据源顺序
        val tempNewSourceList: ArrayList<String> = arrayListOf()
        tempNewSourceList.addAll(sourceList.subList(index, sourceList.size))
        // 转换数据位置，如果需要列表循环播放，那么放开对应的位置
        if (downloadAll) {
            tempNewSourceList.addAll(sourceList.subList(0, index))
        }

        val downloadElementList = ArrayList<TruelyAudioElement>()
        tempNewSourceList.forEach {
            val srcPath = it
            val localPath = TruelyAudioFileManager.getLocalFilePath(App.instance, srcPath)
            downloadElementList.add(TruelyAudioElement(this, srcPath, localPath))
        }
        return downloadElementList
    }

    override fun stop(category: String?) {
        player.finalize()
    }

    override fun stopAll() {

    }

    override fun onStatusChanged(lapt: TruelyAudioPlayer?, status: Int, other: Any?) {
        if (status == TruelyAudioPlayer.Status.STATUS_READY) {
            // ready
            val duration: Int = other as Int
            // 如果当前总时长大于起播时间，释放，并抛出异常，需要重新同步
            if (duration <= startTime) {
                player.finalize()
                callback.invoke(TruelyAudioError.ALARM_SYNC, TruelyAudioError.STR_ALARM_SYNC)
                return
            }
            // 跳转到起播位置
            player.seek(startTime)
        }
    }
}