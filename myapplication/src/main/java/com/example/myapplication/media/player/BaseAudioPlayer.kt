package com.example.myapplication.media.player

import android.util.Log
import androidx.annotation.CallSuper
import com.example.myapplication.media.ITruelyPlayer
import com.example.myapplication.media.TruelyAudioPlayerManager
import com.example.myapplication.media.TruelyAudioStatusCode

/**
 * @author admin
 * @date 2022/5/9
 * @Desc
 */
open class BaseAudioPlayer : ITruelyPlayer, TruelyAudioPlayer.OnStatusChangedListener {
    val player: TruelyAudioPlayer = TruelyAudioPlayer()
    lateinit var callback: (code: Int, msg: String, other: Any?) -> Unit

    @TruelyAudioPlayerManager.AudioMediaCategory
    lateinit var category: String
    lateinit var extra: Any


    @CallSuper
    override fun play(
        source: Any,
        category: String,
        callback: (code: Int, msg: String, other: Any?) -> Unit
    ) {
        this.extra = source
        this.category = category
        this.callback = callback
    }

    @CallSuper
    override fun stop(category: String?) {
        player.finalize()
    }

    override fun stopAll() {

    }

    /**
     * 真正的开始播放
     */
    fun play(localPath: String) {
        Log.d("wangxu3", "BaseAudioPlayer ===> play() called with: localPath = $localPath")
        player.play(localPath)
        player.setStatusChangedListener(TruelyAudioPlayer.Status.STATUS_READY, this)
        player.setStatusChangedListener(TruelyAudioPlayer.Status.STATUS_COMPLETE, this)
        player.setStatusChangedListener(TruelyAudioPlayer.Status.STATUS_STOP, this)
        player.setStatusChangedListener(TruelyAudioPlayer.Status.STATUS_ERROR, this)
    }

    @CallSuper
    override fun onStatusChanged(lapt: TruelyAudioPlayer?, status: Int, other: Any?) {
        if (status == TruelyAudioPlayer.Status.STATUS_READY) {
            // 准备播放，给个回调
            callback.invoke(
                TruelyAudioStatusCode.START_PLAY,
                TruelyAudioStatusCode.STR_START_PLAY,
                null
            )
        } else if (status == TruelyAudioPlayer.Status.STATUS_ERROR) {
            // 播放错误给个回调
            callback.invoke(
                TruelyAudioStatusCode.PLAY_ERROR,
                TruelyAudioStatusCode.STR_PLAY_ERROR,
                null
            )
        }
    }
}