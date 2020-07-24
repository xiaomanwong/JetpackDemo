package com.example.jetpackdemo.control

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener

class CurtainView @JvmOverloads constructor(
    context: Context?,
    attributes: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributes, defStyleAttr) {

    private var mPaint: Paint? = null

    // View 宽高
    private var mWidth: Float = 0F
    private var mHeight: Float = 0F


    // 空白间隔
    private var blockInterval = 0F

    // 画笔宽
    private var paintWidth: Float = 0F

    /**
     * 起点坐标
     * -startX 左侧起点坐标
     * startX 右侧起点坐标
     */
    private var startX: Float = 0F
    private var maxDistance = 0F
    private var minStartX = 0F

    /**
     * 轨道长度
     * 轨道长度用来计算动画执行时间
     * 轨道长度等于最左侧起点绝对值 + 画布一半
     */
    private var traceLength: Float = 0F

    // 动画时间
    private val mDuration = 3000L
    private var animatorOpen: ValueAnimator? = null
    private var animatorClose: ValueAnimator? = null
    private var stateType: String? = "close"

    init {
        // 抗锯齿
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint?.color = Color.parseColor("#474F5D")
        paintWidth = dp2px(6f).toFloat()
        mPaint?.strokeWidth = paintWidth
        mPaint?.strokeCap = Paint.Cap.ROUND
    }

    private fun setOpenState(isOpen: String = "close"): Unit {
        stateType = isOpen
        startX = when (isOpen) {
            "open" -> {
                2 * blockInterval + 3 * paintWidth
            }
            "close" -> {
                5 * blockInterval + 5 * paintWidth + blockInterval + paintWidth
            }
            else -> {
                blockInterval + 2 * paintWidth
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()
        blockInterval = mWidth / 12 - paintWidth
        minStartX = 2 * blockInterval + 3 * paintWidth
        maxDistance =
            5 * blockInterval + 5 * paintWidth + blockInterval + paintWidth - 2 * blockInterval - 3 * paintWidth // 剩余的距离，即终点坐标
        traceLength = minStartX + mWidth / 2;
        setOpenState(stateType!!)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (index in 0..5) {
            val drawX = startX - index * (blockInterval + paintWidth)
            mPaint?.let {
                canvas?.drawLine(
                    drawX, paintWidth,
                    drawX, mHeight - paintWidth - 20, it
                )
            }
            mPaint?.let {
                canvas?.drawPoint(
                    drawX,
                    mHeight - paintWidth, it
                )
            }
        }

        for (index in 0..5) {
            val drawX = mWidth - startX + index * (blockInterval + paintWidth)
            mPaint?.let {
                canvas?.drawLine(
                    drawX, paintWidth,
                    drawX, mHeight - paintWidth - 20,
                    it
                )
            }
            mPaint?.let {
                canvas?.drawPoint(
                    drawX,
                    mHeight - paintWidth, it
                )
            }
        }

        mPaint?.let {
            canvas?.drawLine(
                startX, paintWidth,
                mWidth - startX, paintWidth, it
            )
        }
    }

    fun closeAnimator() {
        if (animatorOpen != null && animatorOpen?.isRunning!!) {
            animatorOpen?.cancel()
        }

        if (animatorClose == null || !animatorClose?.isRunning!!) {
            animatorClose = ValueAnimator.ofFloat(
                startX,
                5 * blockInterval + 5 * paintWidth + blockInterval + paintWidth
            )
            animatorClose?.apply {
                addUpdateListener {
                    startX = it.animatedValue as Float
                    invalidate()
                }
                duration =
                    if (maxDistance - startX == 0F) mDuration else ((maxDistance - (startX - minStartX)) / maxDistance * mDuration).toLong()
                interpolator = LinearInterpolator()
                addListener(onEnd = {
                    cancel()
                })
                start()
            }
        }
    }

    fun openAnimator() {
        if (animatorClose != null && animatorClose?.isRunning!!) {
            animatorClose?.cancel()
        }
        if (animatorOpen == null || !animatorOpen?.isRunning!!) {
            animatorOpen = ValueAnimator.ofFloat(startX, minStartX)
            animatorOpen?.apply {
                addUpdateListener {
                    startX = it.animatedValue as Float
                    invalidate()
                }
                duration =
                    if (startX == maxDistance) mDuration else ((startX - minStartX) / maxDistance * mDuration).toLong()
                println("打开时常： $duration")
                interpolator = LinearInterpolator()
                addListener(onEnd = {
                    cancel()
                })
                start()
            }
        }
    }

    fun stopAnimator() {
        animatorOpen?.cancel()
        animatorClose?.cancel()
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private fun dp2px(dpValue: Float): Int {
        return (resources.displayMetrics.widthPixels * dpValue / 750).toInt()
    }
}

