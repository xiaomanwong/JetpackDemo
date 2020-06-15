package com.example.jetpackdemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ScaleCharListView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private var mWidth: Int = 0
    private var mHeight: Int = 0

    val mPaintNormal = Paint(Paint.ANTI_ALIAS_FLAG)
    val mPaintBigger = Paint(Paint.ANTI_ALIAS_FLAG)
    val mPaintLarge = Paint(Paint.ANTI_ALIAS_FLAG)

    var currentIndex = -1

    init {
        mPaintNormal.color = Color.GRAY
        mPaintBigger.color = Color.DKGRAY
        mPaintLarge.color = Color.BLACK
    }

    val wordArray = arrayOf(
        "A", "B", "C", "D", "E",
        "F", "G", "H", "I", "J", "K",
        "L", "M", "N", "O", "P", "Q",
        "R", "S", "T", "U", "V", "W",
        "X", "Y", "Z"
    )


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)

        var width = 0
        var height = 0

        when (widthMode) {
            MeasureSpec.EXACTLY -> width = widthMode
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED ->{

            }
        }

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        wordArray.forEachIndexed { index, item ->
            if (index == 0) {
                canvas?.drawText(
                    item,
                    (mWidth).toFloat(),
                    (mHeight / wordArray.size).toFloat(), mPaintLarge
                )
            }
        }
    }

}