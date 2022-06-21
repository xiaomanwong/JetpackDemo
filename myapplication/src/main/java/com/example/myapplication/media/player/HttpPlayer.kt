package com.example.myapplication.media.player

import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.myapplication.media.TruelyAudioStatusCode
import org.json.JSONObject

/**
 * @author admin
 * @date 2022/5/10
 * @Desc
 */
class HttpPlayer : OnlineBaseAudioPlayer(), AudioManager.OnAudioFocusChangeListener {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun play(
        source: Any,
        category: String,
        callback: (code: Int, msg: String, other: Any?) -> Unit
    ) {
        super.play(source, category, callback)
        val params = source as JSONObject
        val srcPath = params.optString("source")
        if (srcPath.startsWith("http")) {
            play(srcPath)
        } else {
            callback.invoke(
                TruelyAudioStatusCode.PARAM_ERROR,
                TruelyAudioStatusCode.STR_PARAM_ERROR,
                srcPath
            )
        }
        requestAudioFocusManager()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun requestAudioFocusManager() {
        // 初始化对音频焦点的监听
        val audioAttributes: AudioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        val audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
            .setAudioAttributes(audioAttributes)
            .setAcceptsDelayedFocusGain(true)
            .setWillPauseWhenDucked(true)
            .setOnAudioFocusChangeListener(this)
            .build()

        player.setAttribute(audioAttributes)
        val result = requestAudioFocusManager(audioFocusRequest)
        Log.d("wangxu3", "JukeboxPlayer ===> requestAudioFocusManager() called =======>   $result")
    }

    override fun onStatusChanged(lapt: TruelyAudioPlayer?, status: Int, other: Any?) {
        super.onStatusChanged(lapt, status, other)
        if (status == TruelyAudioPlayer.Status.STATUS_COMPLETE) {
            // 准备播放，给个回调
            callback.invoke(
                TruelyAudioStatusCode.PLAY_COMPLETE,
                TruelyAudioStatusCode.STR_PLAY_SINGLE_COMPLETE,
                null
            )
        }
    }

    override fun onAudioFocusChange(focusChange: Int) {

    }
}