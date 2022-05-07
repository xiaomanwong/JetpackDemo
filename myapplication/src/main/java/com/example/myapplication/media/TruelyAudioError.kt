package com.example.myapplication.media

import androidx.annotation.IntDef
import androidx.annotation.StringDef

/**
 * @author admin
 * @date 2022/5/6
 * @Desc
 */
annotation class TruelyAudioError {


    @Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE_PARAMETER, AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(DOWNLOAD_ERROR, PLAY_COMPLETE, PLAY_LIST_COMPLETE, ALARM_SYNC, OTHER)
    annotation class Code

    @Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE_PARAMETER, AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.SOURCE)
    @StringDef(
        STR_DOWNLOAD_ERROR,
        STR_PLAY_COMPLETE,
        STR_PLAY_LIST_COMPLETE,
        STR_ALARM_SYNC,
        STR_OTHER
    )
    annotation class Message

    companion object {
        const val DOWNLOAD_ERROR = 2001
        const val PLAY_COMPLETE = 2002
        const val PLAY_LIST_COMPLETE = 2003
        const val ALARM_SYNC = 2004
        const val OTHER = 2005


        const val STR_DOWNLOAD_ERROR = "下载失败"
        const val STR_PLAY_COMPLETE = "播放完成"
        const val STR_PLAY_LIST_COMPLETE = "列表播放完成"
        const val STR_ALARM_SYNC = "需要时钟同步"
        const val STR_OTHER = "位置错误"
    }


}