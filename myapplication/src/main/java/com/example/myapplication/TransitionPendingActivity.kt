package com.example.myapplication

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.activity_transition_pending.change_bound
import kotlinx.android.synthetic.main.activity_transition_pending.clip_bound
import kotlinx.android.synthetic.main.activity_transition_pending.root
import kotlinx.android.synthetic.main.activity_transition_pending.scroll_bound
import kotlinx.android.synthetic.main.activity_transition_pending.unknow_bound

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
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                val t = ChangeScroll()
//                TransitionManager.beginDelayedTransition(root, t)
//            }
//            if (scroll_bound.scrollX == -50 && scroll_bound.scrollY == -50) {
//                scroll_bound.scrollTo(0, 0)
//            } else {
//                scroll_bound.scrollTo(-50, -50)
//            }

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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        val colorFrom = resources.getColor(R.color.colorAccent)
        val colorTo = resources.getColor(R.color.colorPrimary)

//        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
//        colorAnimation.duration = 2000L
//
//        colorAnimation.addUpdateListener { animator ->
//            val color = animator.animatedValue as Int
//            val shader = LinearGradient(
//                0f,
//                0f,
//                scroll_bound.maxWidth.toFloat(),
//                scroll_bound.measuredHeight.toFloat(),
//                color,
//                color,
//                Shader.TileMode.CLAMP
//            )
//
//            scroll_bound.paint.shader = shader
//        }
//
//        colorAnimation.start()
        AnimatorSet().apply {

            playTogether(
                ObjectAnimator.ofFloat(scroll_bound, "alpha", 0f, 1f),
                ObjectAnimator.ofArgb(
                    scroll_bound.foreground, "color", Color.TRANSPARENT,
                    Color.parseColor("#0b0b0b"),
                    Color.TRANSPARENT
                ),
                ObjectAnimator.ofFloat(
                    scroll_bound, "scaleX", 0.8f, 1.2f, 1f
                ),
                ObjectAnimator.ofFloat(scroll_bound, "scaleY", 0.8f, 1.2f, 1f)
            )
            duration = 2000
            interpolator = AccelerateDecelerateInterpolator()
        }.start()

    }

    override fun onBackPressed() {
        ActivityCompat.finishAfterTransition(this)
    }
}