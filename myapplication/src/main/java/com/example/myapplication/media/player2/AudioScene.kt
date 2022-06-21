/**
 * 场景播放器
 * @author aosyang
 * @date 2022.05.19
 */
package com.example.myapplication.media.player2

typealias AUDIO_SCENE_NAME_T = String

typealias AUDIO_Z_INDEX_T = Int

data class PlayControl(
    val strategy: AudioBox.PlayStrategy,
    val loopMode: AudioBox.LoopMode,
    val zIndexT: AUDIO_Z_INDEX_T /* = kotlin.Int */,
    val weightT: AUDIO_W_WEIGHT_T /* = kotlin.Int */,
    val musicStart: MusicInputInfo, // 从哪首音乐开始播放
    val startTick: Int // 从音乐的哪个时间点开始播放
)

/**
 * AudioScene 播放域，域对应着业务侧的场景，比如 群空间场景， 视频编辑场景，群聊场景
 * 1. 每个场景（scene）下会有一组播放器 实例，用例处理 互斥 和 叠加 逻辑
 * 2. scene 之间的播放器不存在任何的互斥和叠加逻辑，相互之间不影响
 * scene 内部主要通过：
 * 1. ZIndex 实现 音乐叠加
 * 2. 每个 ZIndex 对应一个播放器实例
 * 3. 播放器内部持有一个 weight 权重，每次播放音乐列表时，需要给出列表权重
 * 4. 权重高，可以覆盖该播放器实例下的音乐列表，否则 新的音乐列表无效
 */
class AudioScene(val scene: AUDIO_SCENE_NAME_T) {
    //
    // ZIndex 代表叠加，每个 ZIndex 对应一个播放器实例，能够播放一组音乐
    //
    private val audioBoxes: MutableMap<AUDIO_Z_INDEX_T, AudioBox> = mutableMapOf()
    private val audioVolume: AudioVolumeStrategy by lazy { AudioVolumeStrategy() }
    fun play(playControl: PlayControl, musics: MutableList<MusicInputInfo>) {
        //
        // check if audiobox created
        //
        // 1. 由于 weight 的限制，处理互斥逻辑，权重低的会直接抛弃这次音乐列表，并返回空
        // 2. 如果返回空，则不再执行 play 操作，意味着本次音乐列表无效
        //
        audioBoxes.getOrPut(playControl.zIndexT) {
            AudioBox(
                playControl.strategy,
                playControl.loopMode,
                audioStatusListener = audioVolume
            )
        }.also {
            it.weight = playControl.weightT
        }.play(
            playControl.weightT,
            playControl.musicStart,
            playControl.startTick,
            musics
        )
    }

    fun position(zIndexT: AUDIO_Z_INDEX_T /* = kotlin.Int */): CurrentPositionInfo {
        return if (audioBoxes.contains(zIndexT)) {
            audioBoxes[zIndexT]!!.position()
        } else {
            CurrentPositionInfo(false, "", 0)
        }
    }

    fun stop() {
        for ((_, audio) in audioBoxes) {
            audio.stop()
        }
    }

    fun pause() {
        for ((_, audio) in audioBoxes) {
            audio.pause()
        }
    }

    fun resume() {
        for ((_, audio) in audioBoxes) {
            audio.resume()
        }
    }
}