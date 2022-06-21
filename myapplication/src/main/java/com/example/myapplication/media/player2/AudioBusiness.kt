/**
 * 针对业务进行特化的接口
 * @author aosyang
 * @date 2022.05.19
 * 这对业务侧进行一次简单的封装，封装主要考虑：
 * 场景方面
 *  （1）群空间场景，有背景音和前景音，通过 zIndex 来区分。比如唱片机 使用 背景音，群空间上的声音，用 前景音
 *  （2）其他场景，全部是前景音，使用一个共同的权重，全部互斥
 */
package com.example.myapplication.media.player2


const val AUDIO_SCENE_SPACE = "audio_scene_space"
const val AUDIO_SCENE_POLLY = AUDIO_SCENE_SPACE
const val AUDIO_SCENE_OTHER = "audio_scene_other"

const val AUDIO_Z_INDEX_GUITAR = 2
const val AUDIO_Z_INDEX_POLLY = 3

/**
 * 这里是业务特例层面
 */
fun playPolly(pollyInfo: MusicInputInfo) {
    AudioManager.instance.play(
        AUDIO_SCENE_POLLY,
        PlayControl(
            AudioBox.PlayStrategy.Sequence,
            AudioBox.LoopMode.Sequence,
            AUDIO_Z_INDEX_POLLY,
            1,
            pollyInfo,
            0
        ),
        mutableListOf(pollyInfo)
    )
}

fun stopAllScene() {
    AudioManager.instance.stopAll()
}