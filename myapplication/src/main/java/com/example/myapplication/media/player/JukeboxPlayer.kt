package com.example.myapplication.media.player

import android.util.Log

/**
 * @author admin
 * @date 2022/5/6
 * @Desc
 */
class JukeboxPlayer : BaseAudioPlayer() {

    override fun play(
        sourceList: ArrayList<String>,
        index: Int,
        startTime: Long,
        category: String,
        callback: (code: Int, msg: String) -> Unit
    ) {
        Log.d(
            "wangxu3",
            "JukeboxPlayer ===> play() called with: sourceList = $sourceList, index = $index, startTime = $startTime, category = $category, callback = $callback"
        )
        super.play(sourceList, index, startTime, category, callback)

    }

    override fun stop(category: String?) {
        super.stop(category)
    }

    override fun stopAll() {

    }

    override fun onStatusChanged(lapt: TruelyAudioPlayer?, status: Int, other: Any?) {
        super.onStatusChanged(lapt, status, other)
        Log.d(
            "wangxu3",
            "JukeboxPlayer ===> onStatusChanged() called with: lapt = $lapt, status = $status, other = $other"
        )
        // 循环播放
        if (status == TruelyAudioPlayer.Status.STATUS_COMPLETE) {
            // 播放完成一首歌，将继续播放列表
            if (++index >= sourceList.size) {
                // 列表播放完成
                index = 0
            }
            play(sourceList, index, 0, category, callback)
        }
    }
}