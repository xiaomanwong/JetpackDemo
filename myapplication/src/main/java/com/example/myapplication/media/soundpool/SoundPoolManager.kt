package com.example.myapplication.media.soundpool

import android.media.AudioAttributes
import android.media.SoundPool

/**
 * @author admin
 * @date 2022/5/18
 * @Desc
 */
object SoundPoolManager {


    private val pool: SoundPool = SoundPool.Builder().apply {
        setMaxStreams(20)
        val audioAttributes: AudioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        setAudioAttributes(audioAttributes)
    }.build()

    private val map: HashMap<Int, Int> = HashMap()

    init {
        pool.setOnLoadCompleteListener{ pool, sampleId, status->
//            map[1]?.let {

                pool.play(sampleId,1f, 1f, 1, 0, 1.0f)
            println( "=============> + ${map[1] == sampleId},   status = $status ")
//            }

        }
    }

    fun play(path: String) {
        val streamId = pool.load(path, 0)
        map[1] = streamId

//        pool.play(streamId, 1f, 1f, 1, 0, 1.0f)
    }
}