package com.example.myapplication.media.player2

/**
 * 定义播放器播放音乐单元信息
 * 音乐可以是远程 url 也可以是本地的， 对于业务侧，只设置 originalUrl
 * @param originalUrl 原始音乐地址
 * @param cacheUrl 本地缓存地址，对于实际的业务方，不需要设置该值
 */
data class MusicInputInfo(
    val id:String,
    val originalUrl: String,
    var cacheUrl: String = ""
) {
    val playUrl: String
        get() = cacheUrl.ifEmpty { originalUrl }
}

/**
 * 获取当前播放器的状态
 * @param playing 是否正在播放
 * @param url 播放的原始 url,注意，必须返回的是业务方传入的原始url，方便业务方做索引
 * @param position 播放的音乐位置
 */
data class CurrentPositionInfo(
    val playing: Boolean = false,
    val url: String = "",
    val position: Int = 0
)