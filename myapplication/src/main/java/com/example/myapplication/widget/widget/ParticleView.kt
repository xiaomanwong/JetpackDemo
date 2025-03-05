package com.example.myapplication.widget.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class ParticleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint().apply {
        color = Color.RED // 粒子颜色，可以调整
    }

    private var particleX = 561f
    private var particleY = 560.25f
    private var rotationAngle = 0f
    private var alphaValue = 0f

    private val particleSize = 30f // 粒子的大小

//    init {
//        startAnimation()
//    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 设置透明度
        paint.alpha = (alphaValue * 255).toInt()

        // 保存当前画布状态
        canvas.save()

        // 将画布平移到粒子的当前位置，并应用旋转
        canvas.translate(particleX, particleY)
        canvas.rotate(rotationAngle)

        // 绘制粒子（圆形表示）
        canvas.drawCircle(0f, 0f, particleSize, paint)

        // 恢复画布状态
        canvas.restore()
    }

    fun startAnimation() {
        // Position Animation using Bezier Curve
        val positionAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 666 // 333ms + 333ms
            interpolator = LinearInterpolator() // 可根据需求自定义

            addUpdateListener { animator ->
                val t = animator.animatedFraction

                // 第一个贝塞尔曲线点
                if (t <= 0.5f) {
                    val bezierT = t * 2
                    particleX = bezierInterpolation(bezierT, 561f, 554.49f, 569f)
                    particleY = bezierInterpolation(bezierT, 560.25f, 251.25f, 557.75f)
                }
                // 第二个贝塞尔曲线点
                else {
                    val bezierT = (t - 0.5f) * 2
                    particleX = bezierInterpolation(bezierT, 554.49f, 569f, 569f)
                    particleY = bezierInterpolation(bezierT, 251.25f, 557.75f, 557.75f)
                }

                invalidate() // 重新绘制视图
            }
        }

        // Rotation Animation
        val rotationAnimator = ValueAnimator.ofFloat(0f, 18.55f, 165f).apply {
            duration = 666 // 333ms + 333ms
            addUpdateListener { animator ->
                rotationAngle = animator.animatedValue as Float
                invalidate()
            }
        }

        // Opacity Animation
        val opacityAnimator = ValueAnimator.ofFloat(0f, 1f, 0f).apply {
            duration = 666 // 333ms + 333ms
            addUpdateListener { animator ->
                alphaValue = animator.animatedValue as Float
                invalidate()
            }
        }

        // 启动所有动画
        positionAnimator.start()
        rotationAnimator.start()
        opacityAnimator.start()
    }

    // 贝塞尔曲线插值器
    private fun bezierInterpolation(t: Float, p0: Float, p1: Float, p2: Float): Float {
        return (1 - t) * (1 - t) * p0 + 2 * (1 - t) * t * p1 + t * t * p2
    }
}