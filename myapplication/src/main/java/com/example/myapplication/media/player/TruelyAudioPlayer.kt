package com.example.myapplication.media.player

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.annotation.FloatRange
import java.io.IOException


/**
 * 真正的播放器
 */
class TruelyAudioPlayer {
    private var mPlayer: MediaPlayer? = MediaPlayer()
    private var _this: TruelyAudioPlayer = this
    private var _volume = 1f
    private var _currentStatus = Status.STATUS_NULL

    //使用Map，而不使用固定变量，方便以后扩展Status
    private val listenerMap: MutableMap<Int, OnStatusChangedListener?> = HashMap()

    /*
     * 播放音频文件
     */
    fun play(sFile: String?) {
        try {
            mPlayer?.let { mPlayer ->

                if (mPlayer.isPlaying) {
                    mPlayer.stop()
                    setCurrentStatus(Status.STATUS_STOP, null)
                }
                mPlayer.reset()
                mPlayer.setDataSource(sFile)
                mPlayer.prepareAsync()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loop(loop: Boolean){
        mPlayer?.isLooping = true
    }

    fun seek(msec: Int) {
        mPlayer?.seekTo(msec)
    }

    /*
     * 设置音量
     */
    fun setVolume(@FloatRange(from = 0.0, to = 1.0) fVal: Float) {
        _volume = fVal
    }

    /*
     * 暂停
     */
    fun pause() {
        mPlayer?.let { mPlayer ->
            if (mPlayer.isPlaying) {
                setCurrentStatus(Status.STATUS_PAUSE, null)
                mPlayer.pause()
            }
        }

    }


    fun setAttribute(attributes: AudioAttributes){
        mPlayer?.setAudioAttributes(attributes)
    }

    /*
     * 恢复播放
     */
    fun resume() {
        if (_currentStatus == Status.STATUS_PAUSE) {
            setCurrentStatus(Status.STATUS_PLAYING, null)
            mPlayer?.start()
        }
    }

    /*
     * 设置当前状态，同时调用相应的回调（如果有）
     */
    private fun setCurrentStatus(nVal: Int, obj: Any?) {
        _currentStatus = nVal
        if (listenerMap.containsKey(nVal)) {
            val listener = listenerMap[nVal]
            listener?.onStatusChanged(_this, nVal, obj)
        }
    }

    interface Status {
        companion object {
            const val STATUS_NULL = 0
            const val STATUS_READY = 1
            const val STATUS_PLAYING = 2
            const val STATUS_PAUSE = 3
            const val STATUS_COMPLETE = 4
            const val STATUS_STOP = 5
            const val STATUS_ERROR = 9
        }
    }

    interface OnStatusChangedListener {
        fun onStatusChanged(lapt: TruelyAudioPlayer?, status: Int, other: Any?)
    }

    fun setStatusChangedListener(
        nStatus: Int,
        listener: OnStatusChangedListener?
    ): TruelyAudioPlayer {
        listenerMap[nStatus] = listener
        return this
    }

    fun finalize() {
        mPlayer?.let { mPlayer ->
            if (mPlayer.isPlaying) mPlayer.stop()
            mPlayer.release()
            this.mPlayer = null
        }

    }

    init {
        mPlayer?.let { mPlayer ->
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mPlayer.setOnPreparedListener { mp ->
                setCurrentStatus(Status.STATUS_READY, mp.duration)
                //region
                mp.setVolume(_volume, _volume)
                mp.start()
                setCurrentStatus(Status.STATUS_PLAYING, null)
                //endregion
            }
            mPlayer.setOnErrorListener { mp, what, extra ->
                if (mPlayer.isPlaying) {
                    mPlayer.stop()
                    setCurrentStatus(Status.STATUS_STOP, null)
                }
                setCurrentStatus(Status.STATUS_ERROR, null)
                false
            }
            mPlayer.setOnCompletionListener {
                //region
                setCurrentStatus(Status.STATUS_COMPLETE, null)
                //endregion
            }
        }
    }
}