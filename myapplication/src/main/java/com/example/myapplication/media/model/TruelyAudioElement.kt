package com.yidian.local.service.audio.model

import com.example.myapplication.media.player.BaseDownloadAndPlayPlayer

/**
 * @author admin
 * @date 2022/5/6
 * @Desc
 */
class TruelyAudioElement(
    val player: BaseDownloadAndPlayPlayer,
    val path: String,
    val localPath: String,
    var startTime: Long = 0,
    var endTime: Long = 0,
    var isFirst: Boolean = false,
    val element: AudioModel
)