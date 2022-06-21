/**
 * 场景播放器
 * @author aosyang
 * @date 2022.05.19
 */
package com.example.myapplication.media.player2


/**
 * 播放管理器，主要是对场景的管理，每个场景对应一个 audiobox 实例
 */
class AudioManager {
    private val scenes: MutableMap<AUDIO_SCENE_NAME_T, AudioScene> = mutableMapOf()

    companion object {
        val instance: AudioManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AudioManager()
        }
    }

    fun play(
        sceneNameT: AUDIO_SCENE_NAME_T,
        control: PlayControl,
        musics: MutableList<MusicInputInfo>
    ) {
        scenes.getOrPut(sceneNameT) {
            AudioScene(sceneNameT)
        }.play(control, musics)
    }

    /**
     * 获取播放器状态
     * 播放器状态与场景和 zIndex 相关，场景和 zIndex 的组合类似 session 的概念
     * @param sceneNameT 场景值
     * @param zIndexT zIndex值
     * @return CurrentPositionInfo 当前的状态值，包括：
     *  （1）是否正在播放，如果不在播放，则 url 和 positio 值无效
     *  （2）播放的原始 url 值
     *  （3）播放的位置
     */
    fun stat(sceneNameT: AUDIO_SCENE_NAME_T, zIndexT: AUDIO_Z_INDEX_T): CurrentPositionInfo {
        return if (scenes.contains(sceneNameT)) {
            scenes[sceneNameT]!!.position(zIndexT)
        } else {
            CurrentPositionInfo(false, "", 0)
        }
    }

    fun pause(sceneNameT: AUDIO_SCENE_NAME_T) {
        scenes[sceneNameT]?.pause()
    }

    fun resume(sceneNameT: AUDIO_SCENE_NAME_T) {
        scenes[sceneNameT]?.resume()
    }

    fun stop(sceneNameT: AUDIO_SCENE_NAME_T) {
        scenes[sceneNameT]?.stop()
    }

    fun stopAll() {
        for ((_, scene) in scenes) {
            scene.stop()
        }
    }
}