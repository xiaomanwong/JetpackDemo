package com.example.customize.widget.loading

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.addListener

class RectLoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var mWidth: Int = 0
    var mHeight: Int = 0

    val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    var isInit = false
    private var rotateYAnimation: ObjectAnimator? = null
    private var rotateXAnimation: ObjectAnimator? = null

    init {
        mPaint.color = Color.RED
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(dp2px(100f), dp2px(100f))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRect(0F, 0F, mWidth.toFloat(), mHeight.toFloat(), mPaint)
        rotateAnimation()
        isInit = true
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private fun dp2px(dpValue: Float): Int {
        return (resources.displayMetrics.widthPixels * dpValue / 750).toInt()
    }

    override fun onDetachedFromWindow() {
        rotateXAnimation?.cancel()
        rotateXAnimation?.removeAllListeners()
        rotateYAnimation?.cancel()
        rotateYAnimation?.removeAllListeners()
        super.onDetachedFromWindow()
    }

    @SuppressLint("WrongConstant")
    private fun rotateAnimation(): Unit {
        if (isInit) return
        println("我开始了")
        rotateYAnimation = ObjectAnimator.ofFloat(this, "rotationX", 180F, 0F)
        rotateYAnimation?.duration = 500
        rotateXAnimation = ObjectAnimator.ofFloat(this, "rotationY", 0F, 180F)
        rotateXAnimation?.duration = 500
        rotateXAnimation?.addListener(onEnd = {
            rotateYAnimation?.start()
        })
        rotateYAnimation?.addListener(onEnd = {
            rotateXAnimation?.start()
        })
        rotateXAnimation?.start()
    }
}