package com.example.jetpackdemo

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class LoadingView @kotlin.jvm.JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val SCALE = 1.0f

    val SCALE_ARRAY = floatArrayOf(SCALE, SCALE, SCALE, SCALE, SCALE, SCALE)
    val START_DELAY = longArrayOf(100, 200, 300, 200, 100)

    var mWidth = 0
    var mHeight = 0

    /**
     * 默认黑柱的宽度，间隔为两倍柱子的宽度
     *
     * 共绘制 17 个
     */
    private var defaultInterval = dp2px(7)

    var isStartAnimator = false


    init {
        mPaint.color = Color.parseColor("#FAFAFA")
        background = resources.getDrawable(R.color.colorAccent, resources.newTheme())
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dp2px(dpValue: Int): Int {
        return (resources.displayMetrics.widthPixels * dpValue / 750)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)


        val width: Int
        val height: Int

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        if (widthMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec)
            height = MeasureSpec.getSize(heightMeasureSpec)
            defaultInterval = width / 18

        } else {
            width = defaultInterval * 18
            height = 150
        }
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        // 设置左右内距离和上下内距
        this.setPadding(
            defaultInterval * 2, defaultInterval / 2, defaultInterval * 2, defaultInterval / 2
        )
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        // 绘制 6 个圆角矩形，空白部分为间隔
        val translateX = defaultInterval
        val translateY = mHeight / 2f
        for (index in 0..4) {
            canvas!!.save()
            canvas.translate(3 * translateX * (index.toFloat() + 1), translateY)
            canvas.scale(SCALE, SCALE_ARRAY[index])
            canvas.drawRoundRect(
                RectF(
                    -defaultInterval.toFloat() / 2,
                    -mHeight / 2.5f,
                    defaultInterval.toFloat() / 2,
                    mHeight / 2.5f
                ),
                defaultInterval / 2f,
                defaultInterval / 2f, mPaint
            )
            canvas.restore()
        }
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == VISIBLE) {
            startAnimator()
        }
    }

    private fun startAnimator() {

        if (isStartAnimator) {
            return
        }

        for (index in 0..4) {

            val annotation = ValueAnimator.ofFloat(1.0f, 0.4f, 1.0f)
            annotation.apply {
                duration = 1000
                repeatCount = -1
                startDelay = START_DELAY[index]
                addUpdateListener {
                    SCALE_ARRAY[index] = it.animatedValue as Float
                    invalidate()
                }
                start()
            }

        }

        isStartAnimator = true

    }

}