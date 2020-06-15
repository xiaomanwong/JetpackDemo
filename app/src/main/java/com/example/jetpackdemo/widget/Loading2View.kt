package com.example.jetpackdemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class Loading2View @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    var mWidth = 0
    var mHeight = 0

    val step = dp2px(40)

    val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        mPaint.color = Color.RED
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dp2px(dpValue: Int): Int {
        return (resources.displayMetrics.widthPixels * dpValue / 750).toInt()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(dp2px(120), dp2px(120))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (i in 1..3) {
            canvas?.drawRect(
                step * (i - 1).toFloat(),
                (mHeight - step * i).toFloat(),
                step * i.toFloat(),
                mHeight - step * (i - 1).toFloat(),
                mPaint
            )
        }

        for (i in 1..2) {
            canvas?.drawRect(
                step * (i - 1).toFloat(),
                (mHeight - step * (1 + i)).toFloat(),
                step * i.toFloat(),
                (mHeight - step * i).toFloat(),
                mPaint
            )


            canvas?.drawRect(
                step * i.toFloat(),
                (mHeight - step * i).toFloat(),
                (step * (1 + i)).toFloat(),
                (mHeight - step * (i - 1)).toFloat(),
                mPaint
            )
        }

        for (i in 1..1) {
            canvas?.drawRect(
                step * (i - 1).toFloat(),
                (mHeight - step * (2 + i)).toFloat(),
                step * i.toFloat(),
                (mHeight - step * (i + 1)).toFloat(),
                mPaint
            )

            canvas?.drawRect(
                step * (i + 1).toFloat(),
                (mHeight - step * (2 - i)).toFloat(),
                (step * (i + 2)).toFloat(),
                (mHeight - step * (i - 1)).toFloat(),
                mPaint
            )
        }


    }
}