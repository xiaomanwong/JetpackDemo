package com.example.myapplication

import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_transition_pending.*
import kotlinx.android.synthetic.main.activity_transition_pending2.*

/**
 * @author admin
 * @date 2022/4/27
 * @Desc
 */
internal class TransitionPendingActivity2 : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transition_pending2)
        window.enterTransition = Slide()
        window.exitTransition = Slide()
        window.reenterTransition = Slide()
        window.returnTransition = Slide()
//        tv_text.text = "页面二"
//        tv_text.setOnClickListener { onBackPressed() }

        video.setVideoPath("https://test-oss-story-uvideo.bondee.ltd/story-uvideo/jp/2023/0602/1685674873134116?timestamp=1685933625&auth_id=test&auth_key=0661ad886e803d387b245b9d7dbd2825")
        video.setOnPreparedListener {
            Log.d("Quinn", "setOnPreparedListener");
        }

        video.setOnInfoListener { mp, what, extra ->
            Log.d("Quinn", "what: $what extra: $extra, buffer: ${video.bufferPercentage}");

            false
        }


        video.setOnCompletionListener {
            Log.d("Quinn", "setOnCompletionListener");
//            video.setVideoPath("https://test-oss-story-uvideo.bondee.ltd/story-uvideo/jp/2023/0602/1685674873134116?timestamp=1685933625&auth_id=test&auth_key=0661ad886e803d387b245b9d7dbd2825")
//            video.start()
        }
        video.start()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        ActivityCompat.finishAfterTransition(this)
    }
}