package com.example.myapplication.media

import android.os.Build
import androidx.annotation.RequiresApi
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
            const val JUKEBOX = "jukebox" // 唱片机
            const val GUITAR_FOLK = "guitar_folk" // 、吉他1
            const val GUITAR_CLASSICAL = "guitar_classical" // 、吉他2
            const val GUITAR_ELECTRIC = "guitar_electric" // 、吉他3
            const val POLLY = "polly" //鹦鹉
            const val HTTP = "http"
            const val LOCAL = "local"
            const val BYTE_DATA = "byte_data"
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun play(
        source: Any,
        category: String,
        callback: (code: Int, msg: String, other: Any?) -> Unit
    ) {
        // 处理冲突业务 唱片机和吉他类型互斥
        // 检查是否是具有互斥性元素
        // 找到对应的互斥元素并停止
        findDiscriminateElementAndStop(category)
        // 处理完互斥元素后，判断当前播放列表中是否有当前元素
        val player: BaseAudioPlayer? = establishPlayer(category)
        if (player == null) {
            callback.invoke(
                TruelyAudioStatusCode.PARAM_ERROR,
                TruelyAudioStatusCode.STR_PARAM_ERROR,
                category
            )
            return
        }
        playerList[category] = player
        // 开始播放
        player.play(source, category, callback)
    }

    /**
     * 寻找到互斥元素
     */
    private fun findDiscriminateElementAndStop(category: String) {
        if (category == AudioMediaCategory.JUKEBOX
            || category == AudioMediaCategory.GUITAR_CLASSICAL
            || category == AudioMediaCategory.GUITAR_ELECTRIC
            || category == AudioMediaCategory.GUITAR_FOLK
        ) {
            // 找到对应的互斥元素并停止
            // 如果是唱片机，找到所有吉他播放器,吉他可能有多个
            val iterator = playerList.iterator()
            while (iterator.hasNext()) {
                val (key, value) = iterator.next()
                var remove = false
                if (category == AudioMediaCategory.JUKEBOX) {
                    if (key == AudioMediaCategory.GUITAR_CLASSICAL
                        || key == AudioMediaCategory.GUITAR_ELECTRIC
                        || key == AudioMediaCategory.GUITAR_FOLK
                    ) {
                        remove = true
                    }
                } else if (category == AudioMediaCategory.GUITAR_CLASSICAL
                    || category == AudioMediaCategory.GUITAR_ELECTRIC
                    || category == AudioMediaCategory.GUITAR_FOLK
                ) {
                    if (key == AudioMediaCategory.JUKEBOX) {
                        remove = true
                    }
                }
                if (remove) {
                    value.stop(key)
                    iterator.remove()
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun establishPlayer(category: String): BaseAudioPlayer? {
        return playerList[category] ?: when (category) {
            AudioMediaCategory.JUKEBOX -> JukeboxPlayer()
            AudioMediaCategory.GUITAR_CLASSICAL,
            AudioMediaCategory.GUITAR_ELECTRIC,
            AudioMediaCategory.GUITAR_FOLK -> GuitarPlayer()
            AudioMediaCategory.POLLY -> PollyPlayer()
            AudioMediaCategory.HTTP -> HttpPlayer()
            AudioMediaCategory.LOCAL -> LocalPlayer()
            AudioMediaCategory.BYTE_DATA -> ByteDataPlayer()
            else -> null
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