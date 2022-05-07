package com.example.myapplication.media.player

import com.example.myapplication.media.TruelyAudioError

/**
 * @author admin
 * @date 2022/5/7
 * @Desc
 */
class PollyPlayer : BaseAudioPlayer() {

    override fun play(
        sourceList: ArrayList<String>,
        index: Int,
        startTime: Long,
        category: String,
        callback: (code: Int, msg: String) -> Unit
    ) {
        super.play(sourceList, index, startTime, category, callback)
    }

    override fun stop(category: String?) {
        super.stop(category)
    }

    override fun stopAll() {
        super.stopAll()
    }

    override fun onStatusChanged(lapt: TruelyAudioPlayer?, status: Int, other: Any?) {
        super.onStatusChanged(lapt, status, other)
        if (status == TruelyAudioPlayer.Status.STATUS_COMPLETE) {
            // 播放完成释放播放器
            callback.invoke(TruelyAudioError.PLAY_COMPLETE, TruelyAudioError.STR_PLAY_COMPLETE)
            player.pause()
        }
    }
}