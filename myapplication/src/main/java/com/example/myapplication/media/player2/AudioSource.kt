/**
 * 音乐库
 * 音乐库主要为 manager 提供远程音乐和本地音乐的映射能力，以及远程音乐的下载缓存能力
 * @author aosyang
 * @date 2022.05.19
 */
package com.example.myapplication.media.player2

import com.example.myapplication.media.TruelyAudioFileManager


class TinyDownloaderManager {
    fun downloader(): TinyDownloader {
        return TinyDownloader()
    }
}

fun isRemoteUrl(path: String): Boolean {
    path.toLowerCase().let {
        return it.startsWith("http") ||
                it.startsWith("https")
    }
}

class AudioSource {
    private val downloadManager = TinyDownloaderManager()

    /**
     * 处理 inputinfo
     * 该函数的主要目的是 检查 originalPath 有没有本地对应的缓存文件
     * 1. 如果有缓存文件， 则 设置 cacheUrl 给播放器使用
     * 2. 如果没有缓存文件，则 启动下载器进行后台下载，下载成功或者失败都可以
     * （1）支持边下边播能力，因此，会主动通知播放器是否下载成功
     * （2）如果本次播放请求没有缓存文件，则直接播放远程文件
     */
    fun handle(
        musics: MutableList<MusicInputInfo>,
        downloadsuccessCallbackT: DownloadSuccess_CALLBACK_T
    ): MutableList<MusicInputInfo> {
        val down = downloadManager.downloader()
        return musics.map {
            //
            // 把远程地址，mapping 到 cache 地址，或者启动 后台线程，进行远程缓存
            //
                music ->
            music.copy(
                cacheUrl = TruelyAudioFileManager.checkCacheAndReturn(music.originalUrl) {
                    //
                    // 本地没有缓存，尝试走下载逻辑，前提是 网络文件
                    //
                    it.takeIf { isRemoteUrl(music.originalUrl) }?.apply {
                        down.addTask(music.originalUrl, it)
                    }
                })
        }.toMutableList().also {
            down.start(downloadsuccessCallbackT)
        }
    }
}