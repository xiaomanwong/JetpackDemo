package com.example.myapplication.media

import androidx.annotation.IntDef
import androidx.annotation.StringDef

/**
 * @author admin
 * @date 2022/5/6
 * @Desc
 */
annotation class TruelyAudioStatusCode {


    @Target(
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.TYPE_PARAMETER,
        AnnotationTarget.FUNCTION
    )
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(DOWNLOAD_ERROR, PLAY_COMPLETE, PLAY_LIST_COMPLETE, ALARM_SYNC, PARAM_ERROR)
    annotation class Code

    @Target(
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.TYPE_PARAMETER,
        AnnotationTarget.FUNCTION
    )
    @Retention(AnnotationRetention.SOURCE)
    @StringDef(
        STR_DOWNLOAD_ERROR,
        STR_PLAY_SINGLE_COMPLETE,
        STR_PLAY_LIST_COMPLETE,
        STR_ALARM_SYNC,
        STR_PARAM_ERROR
    )
    annotation class Message

    companion object {

        const val START_PLAY = 2001
        const val PLAYING = 2008
        const val PLAY_ERROR = 2002
        const val PLAY_COMPLETE = 2003
        const val PLAY_LIST_COMPLETE = 2004
        const val DOWNLOAD_ERROR = 2005
        const val ALARM_SYNC = 2006
        const val PARAM_ERROR = 2007

        const val STR_START_PLAY = "开始播放"
        const val STR_PLAYING = "正在播放的曲目"
        const val STR_DOWNLOAD_ERROR = "下载失败"
        const val STR_PLAY_ERROR = "播放错误"
        const val STR_PLAY_SINGLE_COMPLETE = "单曲播放完成"
        const val STR_PLAY_LIST_COMPLETE = "列表播放完成"
        const val STR_ALARM_SYNC = "需要时钟同步"
        const val STR_PARAM_ERROR = "参数错误"
    }


}