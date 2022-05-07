package com.example.myapplication

import android.os.Bundle
import android.transition.Slide
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
        tv_text.text = "页面二"
        tv_text.setOnClickListener { onBackPressed() }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        ActivityCompat.finishAfterTransition(this)
    }
}