package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.transition.ChangeImageTransform
import android.transition.Fade
import android.transition.Slide
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.transition.ChangeBounds
import androidx.transition.ChangeScroll
import androidx.transition.Scene
import androidx.transition.TransitionManager
import com.example.myapplication.transition.ChangeColorTransition
import kotlinx.android.synthetic.main.activity_transition_pending.*

/**
 * @author admin
 * @date 2022/4/27
 * @Desc
 */
internal class TransitionPendingActivity : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        setContentView(R.layout.activity_transition_pending)

//        window.enterTransition = Fade()
//        window.exitTransition = Slide()
//        window.reenterTransition = Slide()
//        window.returnTransition = Slide()
//        tv_text.text = "页面一"
        unknow_bound.setOnClickListener {
            startActivity(Intent(this, TransitionPendingActivity2::class.java))
//            startActivity(
//                Intent(this, TransitionPendingActivity2::class.java),
//                ActivityOptionsCompat.makeSceneTransitionAnimation(
//                    this
////                    this, tv_text, "text"
//                ).toBundle()
//            )
        }


        change_bound.setOnClickListener {
            TransitionManager.beginDelayedTransition(root, ChangeBounds())
            val lp = change_bound.layoutParams
            if (lp.width == 200) {
                lp.width = 1000
            } else {
                lp.width = 200
            }

            change_bound.layoutParams = lp
        }

        clip_bound.setOnClickListener {
            TransitionManager.beginDelayedTransition(root, androidx.transition.ChangeClipBounds())
            val r = Rect(20, 20, 200, 200)
            if (r == clip_bound.clipBounds) {
                clip_bound.clipBounds = null
            } else {
                clip_bound.clipBounds = r
            }
        }

        scroll_bound.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val t = ChangeScroll()
                TransitionManager.beginDelayedTransition(root, t)
            }
            if (scroll_bound.scrollX == -50 && scroll_bound.scrollY == -50) {
                scroll_bound.scrollTo(0, 0)
            } else {
                scroll_bound.scrollTo(-50, -50)
            }

        }

//        Scene.getCurrentScene(root)
//        Scene.getSceneForLayout(root, R.layout.activity_transition_pending, this)
//        LayoutInflater.from(this).inflate(R.layout.activity_transition_pending, root, false)


//        unknow_bound.setOnClickListener {
//            val changeColorTransition = ChangeColorTransition().apply {
//                duration = 2000
//            }
//            TransitionManager.beginDelayedTransition(root, changeColorTransition)
//            val backgroundDrawable = unknow_bound.background as ColorDrawable
//            if (backgroundDrawable.color == Color.BLUE) {
//                unknow_bound.setBackgroundColor(Color.GREEN)
//            } else {
//                unknow_bound.setBackgroundColor(Color.BLUE)
//            }
//
//        }

    }

    override fun onBackPressed() {
        ActivityCompat.finishAfterTransition(this)
    }
}