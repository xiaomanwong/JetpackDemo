package com.example.myapplication.media.download

import com.example.myapplication.media.player.BaseAudioPlayer

/**
 * @author admin
 * @date 2022/5/6
 * @Desc
 */
class TruelyAudioElement(
    val player: BaseAudioPlayer,
    val path: String,
    val localPath: String,
    var startTime: Long = 0,
    var endTime: Long = 0,
    var isFirst: Boolean = false
)