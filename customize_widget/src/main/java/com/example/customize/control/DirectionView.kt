package com.example.customize.control

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs
import kotlin.math.hypot

class DirectionView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var R: Int = 0
    private var r: Int = 0

    private var clickIndex = -1

    init {
        mPaint.color = Color.RED
        isClickable = true

    }

    private val rectF = RectF()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        R = width / 2
        r = width / 5

        rectF.set(0f, 0f, width.toFloat(), height.toFloat())
        for (i in 1..4) {
            canvas?.save()
            if (clickIndex == i) {
                mPaint.color = Color.parseColor("#979797")
            } else {
                mPaint.color = Color.parseColor("#EBECF0")
            }
            canvas?.rotate(90f * (i - 1), width.toFloat() / 2, height.toFloat() / 2)
            canvas?.drawArc(rectF, -135f, 90f, true, mPaint)
            canvas?.restore()
        }

        mPaint.color = Color.parseColor("#FFFFFF")
        canvas?.drawCircle(width.toFloat() / 2, height.toFloat() / 2, r.toFloat(), mPaint)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event?.action) {
            MotionEvent.ACTION_CANCEL -> {
                println("我走了 Cancel 事件")
            }
            MotionEvent.ACTION_UP -> {
                // 手指抬起，恢复初始状态
                clickIndex = -1
                println("我走了 Up 事件")
                invalidate()
            }
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                // 手指按下，改变画布颜色
                val pX = event.x
                val pY = event.y

                // 将 Android 坐标系转换为 圆坐标系
                val cX = pX - R
                val cY = R - pY

                // 判断点是否在同心圆区域非交集区域
                // 公式 X^2 + y^2 = R^2

                if (R >= hypot(cX, cY)
                    && r < hypot(cX, cY)
                ) {
                    // pX > R， 说明再在圆坐标系的右侧，分布在一四象限
                    if (pX > R) {
                        // pY > r, 说明在圆坐标系的第四象限
                        if (pY > R) {
                            clickIndex = if (abs(cX) > abs(cY)) {
                                // x > y 说明点击区域在旋转后的第二次绘制扇形中
                                2
                            } else {
                                // x < y  说明
                                3
                            }
                        } else {
                            // pY < r 在第一象限
                            clickIndex = if (abs(cX) > abs(cY)) {
                                // x > y 说明点击区域在旋转后的第二次绘制扇形中
                                2
                            } else {
                                // x < y  说明
                                1
                            }
                        }
                    } else if (pX < R) {
                        // px < r,说明在圆坐标系的左侧,分布在 二三象限

                        // pY > r, 说明在圆坐标系的第三象限
                        if (pY > R) {
                            clickIndex = if (abs(cX) > abs(cY)) {
                                // x > y 说明点击区域在旋转后的第二次绘制扇形中
                                4
                            } else {
                                // x < y  说明
                                3
                            }
                        } else {
                            // pY < r 在第二象限
                            clickIndex = if (abs(cX) > abs(cY)) {
                                // x > y 说明点击区域在旋转后的第二次绘制扇形中
                                4
                            } else {
                                // x < y  说明
                                1
                            }
                        }
                    }
                    invalidate()
                }
            }
            else -> {
                return super.onTouchEvent(event)
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dp2px(dpValue: Float): Int {
        return (resources.displayMetrics.widthPixels * dpValue / 750).toInt()
    }
}