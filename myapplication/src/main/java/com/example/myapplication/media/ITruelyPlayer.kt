package com.example.myapplication.media

/**
 * @author admin
 * @date 2022/5/6
 * @Desc
 */
interface ITruelyPlayer {

    fun play(
        sourceList: ArrayList<String>,
        index: Int,
        startTime: Long,
        @TruelyAudioPlayerManager.AudioMediaCategory category: String,
        callback:  (code:Int, msg:String) -> Unit
    )

    fun stop(@TruelyAudioPlayerManager.AudioMediaCategory category: String?)

    // 只用来规范行为，具体 player 不需要重写
    fun stopAll()
}