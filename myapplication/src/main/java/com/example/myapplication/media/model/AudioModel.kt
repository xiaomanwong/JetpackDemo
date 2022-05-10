package com.yidian.local.service.audio.model

/**
 * @author admin
 * @date 2022/5/9
 * @Desc
 */
data class AudioModel(
    val music_id: Int,
    val music_url: String,
    val name: String,
    val signer: String,
    val music_category: String,
    val sort: Int,
    val duration:Int,
    val create_time: String,
    val update_time: String
)