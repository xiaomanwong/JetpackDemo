package com.example.myapplication.media.player2

import androidx.collection.ArrayMap

/**
 *
 * 场景内音频音量控制策略
 *
 * 当场景内同时存在前景音和背景音，背景音音量降低，凸显前景音
 *
 * 当前景音播放完成后，将背景音音量提高
 *
 * 前景音与背景音可以叠加，背景音音量降低
 *
 * 纵向：
 *      前景音与背景音可以叠加，背景音音量降低
 *      只有前景音或背景音音量 1f
 *      前景音播放完成，背景音提高音量
 *
 * 横向：
 *      多个前景音音量相同
 *      多个背景音音量相同
 *
 * @author wangxu
 * @date 2022/5/21
 * @Desc
 */
class AudioVolumeStrategy : AudioStatusChangeListener {

    private val playingAudioBoxes: MutableMap<AudioBox.PlaybackEnvironment, MutableList<AudioBox>> =
        ArrayMap()

    override fun onStartPlay(environment: AudioBox.PlaybackEnvironment, audioBox: AudioBox) {
        playingAudioBoxes.getOrPut(environment) {
            mutableListOf(audioBox)
        }.add(audioBox)
        // 默认音量为 1
        audioBox.setVolume(1f)
        playingAudioBoxes.takeIf { playingAudioBoxes.size == 2 }?.apply {
            getValue(AudioBox.PlaybackEnvironment.Background).forEach { box -> box.setVolume(0.2f) }
        }
    }

    override fun onCompleteOrErrorPlay(
        environment: AudioBox.PlaybackEnvironment,
        audioBox: AudioBox
    ) {
        playingAudioBoxes[environment]?.also {
            it.remove(audioBox)
        }?.takeIf { it.size == 0 }
            ?.apply { playingAudioBoxes.remove(environment) }

        playingAudioBoxes.takeIf { it.size == 1 }?.let {
            it.getValue(AudioBox.PlaybackEnvironment.Background).forEach { box ->
                box.setVolume(1f)
            }
        }
    }

}


interface AudioStatusChangeListener {
    fun onStartPlay(environment: AudioBox.PlaybackEnvironment, audioBox: AudioBox)
    fun onCompleteOrErrorPlay(environment: AudioBox.PlaybackEnvironment, audioBox: AudioBox)
}