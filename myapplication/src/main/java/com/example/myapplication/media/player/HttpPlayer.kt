package com.example.myapplication.media.player

import com.example.myapplication.media.TruelyAudioStatusCode
import org.json.JSONObject

/**
 * @author admin
 * @date 2022/5/10
 * @Desc
 */
class HttpPlayer : OnlineBaseAudioPlayer() {


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
}