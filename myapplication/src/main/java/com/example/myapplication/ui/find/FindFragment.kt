package com.example.myapplication.ui.find

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.lib_annotation.FragmentDestination
import com.example.myapplication.R
import com.example.myapplication.media.TruelyAudioPlayerManager
import com.example.myapplication.media.TruelyAudioStatusCode
import com.yidian.local.service.audio.model.AudioModel
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@FragmentDestination(pageUrl = "main/tabs/find", asStart = false)
class FindFragment : Fragment() {

    private lateinit var findViewModel: FindViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        findViewModel =
            ViewModelProviders.of(this).get(FindViewModel::class.java)
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apply = JSONObject()
        val array = JSONArray()
        arrayListOf(
            AudioModel(
                1,
                "https://shenbian-yidian.go2yd.com/admin/pictools/Amatorski - Come Home.mp3",
                "未知",
                "sss",
                "category",
                1,
                100,
                "12049124",
                "12124124"
            ),
            AudioModel(
                1,
                "https://shenbian-yidian.go2yd.com/admin/pictools/Ginger Root - B4.mp3",
                "未知",
                "sss",
                "category",
                1,
                1000,
                "12049124L",
                "12124124L"
            ), AudioModel(
                1,
                "https://shenbian-yidian.go2yd.com/admin/pictools/KΛGWΣ - ㅤㅤ.mp3",
                "未知",
                "sss",
                "category",
                1,
                100,
                "12049124",
                "12124124"
            ), AudioModel(
                1,
                "https://shenbian-yidian.go2yd.com/admin/pictools/the girl next door - letters to ana.mp3",
                "未知",
                "sss",
                "category",
                1,
                100,
                "12049124L",
                "12124124L"
            )
        ).forEach {
            val obj = JSONObject()
            obj.put("music_id", it.music_id)
            obj.put("music_url", it.music_url)
            obj.put("name", it.name)
            obj.put("signer", it.signer)
            obj.put("music_category", it.music_category)
            obj.put("sort", it.sort)
            obj.put("duration", it.duration)
            obj.put("create_time", it.create_time)
            obj.put("update_time", it.update_time)
            array.put(obj)
        }
        apply.put("sourceList", array)
        apply.put("index", 2)
        apply.put("startTime", 0)
        TruelyAudioPlayerManager.AudioMediaCategory.JUKEBOX
        view.findViewById<View>(R.id.text_dashboard_1).setOnClickListener {
            TruelyAudioPlayerManager.getInstance().play(
                apply,
                TruelyAudioPlayerManager.AudioMediaCategory.JUKEBOX
            ) { code, msg, any ->
                Log.d(
                    "wangxu3",
                    "FindFragment ===> onViewCreated() called with: code = $code, msg = $msg, any = $any"
                )


                if (code != TruelyAudioStatusCode.ALARM_SYNC
                    && code != TruelyAudioStatusCode.START_PLAY
                ) {
                    // 只有同步时钟和开始播放，不需要释放资源，其他情况都回收资源
                    TruelyAudioPlayerManager.getInstance()
                        .stop(TruelyAudioPlayerManager.AudioMediaCategory.JUKEBOX)
                }
            }
        }
        val it = AudioModel(
            1,
            "https://shenbian-yidian.go2yd.com/admin/pictools/the girl next door - letters to ana.mp3",
            "未知",
            "sss",
            "category",
            1,
            100,
            "12049124L",
            "12124124L"
        )
        val obj = JSONObject()
        obj.put("music_id", it.music_id)
        obj.put(
            "music_url",
            "https://shenbian-yidian.go2yd.com/admin/pictools/the girl next door - letters to ana.mp3"
        )
        obj.put("name", it.name)
        obj.put("signer", it.signer)
        obj.put("music_category", it.music_category)
        obj.put("sort", it.sort)
        obj.put("duration", it.duration)
        obj.put("create_time", it.create_time)
        obj.put("update_time", it.update_time)
//
        view.findViewById<View>(R.id.text_dashboard).setOnClickListener {
            TruelyAudioPlayerManager.getInstance().play(
                JSONObject().apply { put("source", obj) },
                TruelyAudioPlayerManager.AudioMediaCategory.GUITAR_FOLK
            ) { code, msg, any ->
                Log.d(
                    "wangxu3",
                    "FindFragment ===> onViewCreated() called with: code = $code, msg = $msg, any = $any"
                )


                if (code != TruelyAudioStatusCode.ALARM_SYNC
                    && code != TruelyAudioStatusCode.START_PLAY
                ) {
                    // 只有同步时钟和开始播放，不需要释放资源，其他情况都回收资源
                    TruelyAudioPlayerManager.getInstance()
                        .stop(TruelyAudioPlayerManager.AudioMediaCategory.GUITAR_FOLK)
                }
            }
        }

        val obj1 = JSONObject()
        obj1.put("music_id", it.music_id)
        obj1.put("music_url", "https://shenbian-yidian.go2yd.com/admin/pictools/KΛGWΣ - ㅤㅤ.mp3")
        obj1.put("name", it.name)
        obj1.put("signer", it.signer)
        obj1.put("music_category", it.music_category)
        obj1.put("sort", it.sort)
        obj1.put("duration", it.duration)
        obj1.put("create_time", it.create_time)
        obj1.put("update_time", it.update_time)

        view.findViewById<View>(R.id.text_dashboard_2).setOnClickListener {
            TruelyAudioPlayerManager.getInstance().play(
                JSONObject().apply { put("source", obj1) },
                TruelyAudioPlayerManager.AudioMediaCategory.GUITAR_CLASSICAL
            ) { code, msg, any ->
                Log.d(
                    "wangxu3",
                    "FindFragment ===> onViewCreated() called with: code = $code, msg = $msg, any = $any"
                )
                if (code != TruelyAudioStatusCode.ALARM_SYNC
                    && code != TruelyAudioStatusCode.START_PLAY
                ) {
                    // 只有同步时钟和开始播放，不需要释放资源，其他情况都回收资源
                    TruelyAudioPlayerManager.getInstance()
                        .stop(TruelyAudioPlayerManager.AudioMediaCategory.GUITAR_CLASSICAL)
                }
            }
        }

        view.findViewById<View>(R.id.text_dashboard_3).setOnClickListener {
            TruelyAudioPlayerManager.getInstance().play(
                JSONObject().apply {
                    put(
                        "source",
//                    "/data/user/0/com.example.myapplication/cache/audio/Amatorski - Come Home.mp3"
                        "https://shenbian-yidian.go2yd.com/admin/pictools/KΛGWΣ - ㅤㅤ.mp3"
                    )
                },
                TruelyAudioPlayerManager.AudioMediaCategory.HTTP
            ) { code, msg, any ->
                Log.d(
                    "wangxu3",
                    "FindFragment ===> onViewCreated() called with: code = $code, msg = $msg, any = $any"
                )
                if (code != TruelyAudioStatusCode.ALARM_SYNC
                    && code != TruelyAudioStatusCode.START_PLAY
                ) {
                    // 只有同步时钟和开始播放，不需要释放资源，其他情况都回收资源
                    TruelyAudioPlayerManager.getInstance()
                        .stop(TruelyAudioPlayerManager.AudioMediaCategory.HTTP)
                }
            }
        }

        File("/data/user/0/com.example.myapplication/cache/audio/KΛGWΣ - ㅤㅤ.mp3")
        val b: ByteArray? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Files.readAllBytes(Paths.get("/data/user/0/com.example.myapplication/cache/audio/KΛGWΣ - ㅤㅤ.mp3"))
        } else {
            null
        }

       val byte :String =  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(b)
        } else {
            ""
       }
        Log.d(
            "wangxu3",
            "FindFragment ===> onViewCreated() called with: byte $byte"
        )
        view.findViewById<View>(R.id.text_dashboard_4).setOnClickListener {
            TruelyAudioPlayerManager.getInstance().play(
                JSONObject().apply {
                    put(
                        "source",
//                    "/data/user/0/com.example.myapplication/cache/audio/Amatorski - Come Home.mp3"
                        byte+"---"
                    )
                },
                TruelyAudioPlayerManager.AudioMediaCategory.BYTE_DATA
            ) { code, msg, any ->
                Log.d(
                    "wangxu3",
                    "FindFragment ===> onViewCreated() called with: code = $code, msg = $msg, any = $any"
                )
                if (code != TruelyAudioStatusCode.ALARM_SYNC
                    && code != TruelyAudioStatusCode.START_PLAY
                ) {
                    // 只有同步时钟和开始播放，不需要释放资源，其他情况都回收资源
                    TruelyAudioPlayerManager.getInstance()
                        .stop(TruelyAudioPlayerManager.AudioMediaCategory.BYTE_DATA)
                }
            }
        }
        view.findViewById<View>(R.id.close_1).setOnClickListener {
            TruelyAudioPlayerManager.getInstance()
                .stop(TruelyAudioPlayerManager.AudioMediaCategory.JUKEBOX)
        }
        view.findViewById<View>(R.id.close_2).setOnClickListener {
            TruelyAudioPlayerManager.getInstance()
                .stop(TruelyAudioPlayerManager.AudioMediaCategory.GUITAR_FOLK)
        }

//
        view.findViewById<View>(R.id.close_3).setOnClickListener {
            TruelyAudioPlayerManager.getInstance()
                .stop(TruelyAudioPlayerManager.AudioMediaCategory.GUITAR_CLASSICAL)
        }

        view.findViewById<View>(R.id.close_4).setOnClickListener {
            TruelyAudioPlayerManager.getInstance()
                .stop(TruelyAudioPlayerManager.AudioMediaCategory.HTTP)
        }
        view.findViewById<View>(R.id.close_5).setOnClickListener {
            TruelyAudioPlayerManager.getInstance()
                .stop(TruelyAudioPlayerManager.AudioMediaCategory.BYTE_DATA)
        }
    }
}
