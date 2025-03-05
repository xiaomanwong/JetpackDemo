package com.example.myapplication.widget.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.PathInterpolator
import android.widget.FrameLayout
import androidx.core.animation.doOnEnd
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ParticleAnimationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    private val flowerPaths: List<List<FlowerPath>>
    private val animatorList = mutableListOf<Animator>()

    private val basePx = 1126

    private var animatorSet: AnimatorSet? = null
    private var isCanceled =false

    init {
        context.assets.open("emoji_fire_flower.json").use { inputStream ->
            val json = inputStream.bufferedReader().use { it.readText() }
            flowerPaths = Gson().fromJson(
                json, object : TypeToken<List<List<FlowerPath>>>() {}.type
            )
        }

    }

    fun setEmoji(emoji: String) {
        repeat(flowerPaths.size) {
            addView(ParticleView(context).apply {
                x = flowerPaths[it].first().position.start.x / basePx * width
                y = flowerPaths[it].first().position.start.y / basePx * height
                alpha = 0f
                setEmoji(emoji)
            }, ViewGroup.LayoutParams(100, 100))
        }
        animateParticle()
    }

    fun startAnimation() {
        isCanceled= false
        animatorSet = AnimatorSet().apply {
            playTogether(animatorList)
            doOnEnd { if (!isCanceled) startAnimation() }
            start()
        }
    }

    fun stopAnimation() {
        isCanceled = true
        animatorSet?.cancel()
        repeat(childCount) {
            getChildAt(it).alpha = 0f
        }
    }

    private fun animateParticle() {
        flowerPaths.forEachIndexed { index, first ->
            val particle = getChildAt(index) as ParticleView
            var firstDuration = 0L
            first.forEachIndexed { childIndex, second ->
                val path = Path()
                path.moveTo(
                    second.position.start.x / basePx * width,
                    second.position.start.y / basePx * height
                )
                path.lineTo(
                    second.position.end.x / basePx * width,
                    second.position.end.y / basePx * height,
                )
                if (childIndex == 0) {
                    firstDuration = second.duration
                }
                val positionAnimator1 =
                    ObjectAnimator.ofFloat(particle, View.X, View.Y, path).apply {
                        duration = second.duration
                        interpolator = PathInterpolator(
                            second.position.control.control1.x,
                            second.position.control.control1.y,
                            second.position.control.control2.x,
                            second.position.control.control2.y,
                        )
                        startDelay = second.startDelay + if (childIndex == 0) 0 else firstDuration
                    }

                val rotationAnimator1 = ObjectAnimator.ofFloat(
                    particle, View.ROTATION, second.rotation.start, second.rotation.end
                ).apply {
                    duration = second.duration
                    interpolator = PathInterpolator(
                        second.rotation.control.control1.x,
                        second.rotation.control.control1.y,
                        second.rotation.control.control2.x,
                        second.rotation.control.control2.y,
                    )
                    startDelay = second.startDelay + if (childIndex == 0) 0 else firstDuration
                }

                val opacityAnimator1 = ObjectAnimator.ofFloat(
                    particle, View.ALPHA, second.alpha.start, second.alpha.end
                ).apply {
                        duration = second.duration
                        interpolator = PathInterpolator(
                            second.alpha.control.control1.x,
                            second.alpha.control.control1.y,
                            second.alpha.control.control2.x,
                            second.alpha.control.control2.y,
                        )
                        startDelay = second.startDelay + if (childIndex == 0) 0 else firstDuration
                    }
                animatorList.add(positionAnimator1)
                animatorList.add(rotationAnimator1)
                animatorList.add(opacityAnimator1)
                if (childIndex != 0) {
                    path.reset()
                }
            }
        }

    }


    // Custom particle view class to represent the particle
    class ParticleView(context: Context) : View(context) {

        private var emoji = "\uD83C\uDF89"
        fun setEmoji(emoji: String) {
            this.emoji = emoji
        }

        private val paint = Paint().apply {
            color = Color.RED
            style = Paint.Style.FILL
            textSize = 56f
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            canvas.drawText(emoji, 0f, height / 2f, paint)
        }
    }


    data class FlowerPath(
        val position: PathPosition,
        val rotation: RotationPath,
        val alpha: AlphaPath,
        val startDelay: Long,
        val duration: Long
    )


    data class RotationPath(
        val start: Float,
        val end: Float,
        val control: BezierControlPoint,
    )

    data class AlphaPath(
        val start: Float,
        val end: Float,
        val control: BezierControlPoint,
    )

    data class PathPosition(
        val start: Position,
        val end: Position,
        val control: BezierControlPoint,
    )

    data class BezierControlPoint(
        val control1: ControlPoint,
        val control2: ControlPoint,
    )

    data class Position(val x: Float, val y: Float)
    data class ControlPoint(val x: Float, val y: Float)

}