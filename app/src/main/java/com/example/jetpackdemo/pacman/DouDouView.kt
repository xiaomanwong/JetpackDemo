package com.example.jetpackdemo.pacman

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class DouDouView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    var mWidth = 0
    var mHeight = 0

    val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    val mPaintBoll = Paint(Paint.ANTI_ALIAS_FLAG)

    var alpha = 255
    var doudouX = 0f
    var startAngle = 40f

    init {
        mPaint.color = Color.RED
        mPaintBoll.color = Color.RED
    }

    var isInit = false

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dp2px(dpValue: Int): Int {
        return (resources.displayMetrics.widthPixels * dpValue / 750).toInt()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(dp2px(100), dp2px(100))
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        doudouX = (mWidth - 10).toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawArc(
            RectF(0f, 30f, mWidth.toFloat() - 30f, mHeight.toFloat()),
            startAngle,
            360 - 2 * startAngle,
            true,
            mPaint
        )

        mPaintBoll.alpha = this.alpha
        canvas?.drawCircle(doudouX, mHeight / 2.toFloat() + 15, 15f, mPaintBoll)
        startAnimator()
        isInit = true
    }


    fun startAnimator(): Unit {
        if (isInit) {
            return
        }

        val animator = ValueAnimator.ofFloat(startAngle, 0f)
        animator.addUpdateListener {
            startAngle = it.animatedValue as Float
            invalidate()
        }
        animator.interpolator = LinearInterpolator()
        animator.repeatMode = ValueAnimator.REVERSE
        animator.repeatCount = -1
        animator.setDuration(400)
//        animator.start()


        val animator1 = ValueAnimator.ofFloat(doudouX, mWidth.toFloat() / 2 - 28f)
        animator1.addUpdateListener {
            doudouX = it.animatedValue as Float
            invalidate()
        }
        animator1.interpolator = LinearInterpolator()
        animator1.repeatMode = ValueAnimator.RESTART
        animator1.repeatCount = -1
        animator1.setDuration(800)
//        animator1.start()

        val animatorAlpha = ValueAnimator.ofInt(200, 255)
        animatorAlpha.apply {
            addUpdateListener {
                alpha = it.animatedValue as Int
                invalidate()
            }
//            start()
        }


        val animatorSet = AnimatorSet()

        animatorSet.playTogether(animator, animator1,animatorAlpha )
        animatorSet.start()

    }

}