package com.example.myapplication.media.player

import android.content.Context
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import android.util.Log
import androidx.annotation.CallSuper
import androidx.annotation.RequiresApi
import com.example.myapplication.App
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
        if (::focusRequest.isInitialized) {
            abandonAudioFocusRequest(focusRequest)
        }
        player.finalize()
    }

    override fun stopAll() {

    }


    /**
     * 获取音频管理器
     */
    private lateinit var audioManager: AudioManager

    private lateinit var focusRequest: AudioFocusRequest

    @RequiresApi(Build.VERSION_CODES.O)
    fun requestAudioFocusManager(focusRequest: AudioFocusRequest): Int {
        audioManager =
            App.instance.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        this.focusRequest = focusRequest
        return audioManager.requestAudioFocus(focusRequest)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun abandonAudioFocusRequest(focusRequest: AudioFocusRequest) {
        Log.d(
            "wangxu3",
            "BaseAudioPlayer ===> abandonAudioFocusRequest() called with: focusRequest = $focusRequest"
        )
        if (::audioManager.isInitialized) {
            audioManager.abandonAudioFocusRequest(
                focusRequest
            )
        }
    }

    /**
     * 真正的开始播放
     */
    fun play(localPath: String) {
        Log.d("wangxu3", "BaseAudioPlayer ===> play() called with: localPath = $localPath")
        player.play(localPath)
        player.setStatusChangedListener(TruelyAudioPlayer.Status.STATUS_READY, this)
        player.setStatusChangedListener(TruelyAudioPlayer.Status.STATUS_COMPLETE, this)
        player.setStatusChangedListener(TruelyAudioPlayer.Status.STATUS_PLAYING, this)
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