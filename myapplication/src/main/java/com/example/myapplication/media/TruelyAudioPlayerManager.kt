package com.example.myapplication.media

import com.example.myapplication.media.player.*


/**
 * @author admin
 * @date 2022/5/6
 * @Desc
 */
class TruelyAudioPlayerManager : ITruelyPlayer {
    /**
     * 音频类型
     */
    @Target(
        AnnotationTarget.TYPE_PARAMETER,
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY,
        AnnotationTarget.TYPE
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class AudioMediaCategory {
        companion object {
            const val BGM = "bgm"
            const val JUKEBOX = "jukebox" // 唱片机
            const val GUITAR = "guitar" // 、吉他
            const val VOICE_MESSAGE = "voice_message" //语音消息
            const val POLLY = "POLLY" //鹦鹉
            const val DEFAULT = "default"
        }
    }

    companion object {

        private var instance: TruelyAudioPlayerManager? = null

        fun getInstance(): TruelyAudioPlayerManager {
            if (instance == null) {
                @Synchronized
                if (instance == null) {
                    instance = TruelyAudioPlayerManager()
                }
            }
            return instance as TruelyAudioPlayerManager
        }
    }

    private val playerList = hashMapOf<@AudioMediaCategory String, BaseAudioPlayer>()

    override fun play(
        sourceList: ArrayList<String>,
        index: Int,
        startTime: Long,
        category: String,
        callback: (code: Int, msg: String) -> Unit
    ) {
        // 处理冲突业务 唱片机和吉他类型互斥
        // 检查是否是具有互斥性元素
        if (category == AudioMediaCategory.JUKEBOX
            || category == AudioMediaCategory.GUITAR
        ) {
            // 找到对应的互斥元素并停止
            findDiscriminateElementAndStop(category)
        }
        // 处理完互斥元素后，判断当前播放列表中是否有当前元素
        val player = establishPlayer(category)
        playerList[category] = player
        // 开始播放
        player.play(sourceList, index, startTime, category, callback)
    }

    /**
     * 寻找到互斥元素
     */
    private fun findDiscriminateElementAndStop(category: String) {
        if (category == AudioMediaCategory.JUKEBOX) {
            // 如果是唱片机，找到所有吉他播放器,吉他可能有多个
            for ((k, v) in playerList) {
                if (k == AudioMediaCategory.GUITAR) {
                    v.stop(category)
                    playerList.remove(k)
                }
            }
        } else if (category == AudioMediaCategory.GUITAR) {
            for ((k, v) in playerList) {
                // 唱片机移除
                if (k == AudioMediaCategory.JUKEBOX) {
                    v.stop(category)
                    playerList.remove(k)
                    break
                }
            }
        }
    }

    /**
     * 构建 player
     *
     * 如果当前播放器正在播放，直接返回
     * 如果当前播放器不存在，则重新创建
     */

    private fun establishPlayer(category: String): BaseAudioPlayer {
        return playerList[category] ?: when (category) {
            AudioMediaCategory.JUKEBOX -> JukeboxPlayer()
            AudioMediaCategory.GUITAR -> GuitarPlayer()
            AudioMediaCategory.POLLY -> PollyPlayer()
            else -> DefaultPlayer()
        }
    }


    /**
     * 停止播放，并释放播放器
     */
    override fun stop(category: String?) {
        playerList[category]?.stop(category)
        playerList.remove(category)
    }

    /**
     * 停止所有播放器播放
     */
    override fun stopAll() {
        for ((k, v) in playerList) {
            v.stop(k)
        }
        playerList.clear()
    }


}