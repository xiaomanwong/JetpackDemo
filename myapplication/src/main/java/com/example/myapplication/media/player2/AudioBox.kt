/**
 * 场景播放器
 * @author aosyang
 * @date 2022.05.19
 */
package com.example.myapplication.media.player2

import android.media.MediaPlayer
import kotlin.random.Random

typealias AUDIO_W_WEIGHT_T = Int

/**
 * 播放器 box
 * 封装 media player，根据一定的策略和循环设置完成列表播放
 * 1. 音乐源是一个列表，播放器播放的是列表逻辑
 * 2. 音乐源列表可以动态替换，一旦发生动态替换，则停止源列表播放过程，重新开始新的播放序列
 * 3. 策略和模式一旦确定，无法更改
 * 4. 只有音乐播放源可以更改，更改播放源的时候，会触发播放器的停止，但是会复用播放器实例
 */
class AudioBox(
    private val strategy: PlayStrategy = PlayStrategy.Sequence,
    private val loopMode: LoopMode = LoopMode.Infinite,
    private val environment: PlaybackEnvironment = PlaybackEnvironment.Foreground,
    private val audioStatusListener: AudioStatusChangeListener
) {
    /**
     * 播放音乐列表策略
     */
    enum class PlayStrategy {
        Sequence, // 顺序播放
        Random, // 随机播放
    }

    /**
     * 循环模式
     */
    enum class LoopMode {
        Infinite, // 无限循环
        Sequence, // 列表播放完成停止
        SingleLoop, //单曲循环
        ListLoop, // 列表循环
        Random // 随机播放
    }

    enum class PlaybackEnvironment {
        Background,
        Foreground
    }

    /**
     * 播放音乐列表的状态
     */
    enum class Status {
        Initial, // 未开始
        Playing, // 播放中
        Paused, // 暂停
        Stopped, //播放完成
        Error // 播放出错
    }

    /**
     * 播放控制
     * 主要是指播放音乐源，以及从该音乐的哪个位置开始播放
     */
    data class NextControl(val music: Int = 0, val startTick: Int = 0)


    private val musicList: ArrayList<MusicInputInfo> = arrayListOf() // 音乐列表
    private var currentIndex = 0 // 当前播放音乐索引
    private var status = Status.Initial // 播放器状态
    var weight: AUDIO_W_WEIGHT_T = 1 // 默认音乐列表权重

    val player: MediaPlayer by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        MediaPlayer().apply {
            setOnCompletionListener {
                status = Status.Stopped
                audioStatusListener.onCompleteOrErrorPlay(environment, this@AudioBox)
                generateNextControl()?.let {
                    playNext(it)
                }
            }
            setOnErrorListener { _, _, _ ->
                status = Status.Error
                audioStatusListener.onCompleteOrErrorPlay(environment, this@AudioBox)
                false
            }
            setOnPreparedListener {

            }
        }
    }

    /**
     * 产生下一个播放源
     * 由于循环策略的控制，可能没有播放源，这里不允许修改播放器的状态 status。状态的修改交给 listener 来完成
     */
    private fun generateNextControl(): NextControl? {
        if (currentIndex >= musicList.size) {
            if (loopMode == LoopMode.Sequence) {
                return null
            }
            return NextControl(
                0, 0
            )
        } else {
            if (loopMode == LoopMode.SingleLoop) {
                return NextControl(currentIndex, 0)
            }

            if (loopMode == LoopMode.Random) {
                val random = Random.nextInt(0, musicList.size)
                currentIndex = random
                return NextControl(currentIndex, 0)
            }
            return NextControl(
                if (strategy == PlayStrategy.Sequence) ++currentIndex else currentIndex,
                0
            )
        }
    }

    /**
     * 播放功能
     * 播放函数需要的是一个播放列表，以及初始播放音乐和该音乐的起点位置
     * @param weightT 权重，权重值决定了 本次播放是否有效，如果本次音乐播放权重大于等于以前的权重，则采取覆盖播放
     * @param music 起始播放音乐名字
     * @param start 播放起始点
     * @param list 音乐播放列表， {music} 必须存在于 {list} 中
     */
    fun play(
        weightT: AUDIO_W_WEIGHT_T,
        music: MusicInputInfo,
        start: Int,
        list: MutableList<MusicInputInfo>
    ) {
        val audioSource = AudioSource()
        val musics = audioSource.handle(list) { remote, local ->

            musicList.find { it.originalUrl == remote }?.run {
                cacheUrl = local
            }
        }

        //
        // 如果权重值太小，对播放器不产生任何影响
        //
        this.takeIf { weightT >= weight }?.apply {
            player.stop()
            musicList.addAll(musics)
            currentIndex = musicList.indexOf(music)
            currentIndex = if (currentIndex == -1) currentIndex else 0
            weight = weightT
        }?.apply {
            NextControl(
                currentIndex,
                start
            ).let {
                playNext(it)
            }
        }
    }

    fun setVolume(volume: Float) {
        player.setVolume(volume, volume)
    }

    private fun playNext(nextControl: NextControl) {
        player.apply {
            status = Status.Playing
            setDataSource(musicList[nextControl.music].playUrl)
            prepare()
            audioStatusListener.onStartPlay(environment, this@AudioBox)
            start()
            seekTo(nextControl.startTick)
        }
    }

    /**
     * 获取当前播放位置：播放状态，播放音乐源 ，以及 音乐位置
     * @see CurrentPositionInfo
     */
    fun position(): CurrentPositionInfo {
        return CurrentPositionInfo(
            playing = status == Status.Playing,
            url = currentIndex.let {
                if (it < musicList.size) {
                    musicList[currentIndex].originalUrl
                } else {
                    ""
                }
            },
            position = player.currentPosition
        )
    }


    fun pause() {
        player.pause()
        status = Status.Paused
    }

    fun resume() {
        player.start()
        status = Status.Playing
    }

    fun stop() {
        player.stop()
        musicList.clear()
        status = Status.Stopped
        currentIndex = 0
    }
}