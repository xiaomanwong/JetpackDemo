package com.example.myapplication.widget.widget.password

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class GesturePasswordPreView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var isError = false
    private var mWidth = 0
    private var mHeight = 0
    private var mR: Int = 0
    private var mDistance = 0

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val markedPoints = LinkedHashMap<Int, PointF>(9)
    private val pointsMap = HashMap<Int, PointF>(9)

    /**
     * [0]: 灰色
     * [1]: 绿色
     * [2]: 红色
     */
    private val colorArray = arrayListOf("#C3C5D2", "#28BC7C", "#D14646")

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val width: Int
        val height: Int
        if (widthMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.EXACTLY) {
            width = widthSize
            height = heightSize
        } else {
            // 为计算和另外一个手势图的适配，宽度设置为另一个视图的一个圆的宽高
            width = (resources.displayMetrics.widthPixels) / 9 * 2
            height = width
        }
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        mR = (mWidth - paddingStart - paddingEnd) / 9
        mDistance = mR * 3
        this.setPadding(
            paddingStart + mR / 2,
            paddingTop + mR / 2,
            paddingEnd + mR / 2,
            paddingBottom + mR / 2
        )
        createGraph()
    }

    private fun createGraph() {
        var centerX: Int
        var centerY: Int

        var index = 0
        for (row in 0..2) {
            for (column in 0..2) {
                centerX = paddingStart + mR + column * mDistance
                centerY = paddingTop + mR + row * mDistance
                pointsMap[index] = PointF(centerX.toFloat(), centerY.toFloat())
                index++
            }
        }
    }

    /**
     * 更新标记点
     */
    fun updatePoint(index: Int) {
        pointsMap[index]?.let { markedPoints[index] = it }
        println("index: $index pointsMap[index] = ${pointsMap[index]} markedPoints[index] = ${markedPoints[index]}")
        invalidate()
    }

    /**
     * 重置为初始状态
     */
    fun resetView(): Unit {
        markedPoints.clear()
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        pointsMap.entries.forEach {
            val index = it.key
            val mutableEntry = it.value
            if (markedPoints.containsKey(index) && !isError) {
                mPaint.color = Color.parseColor(colorArray[1])
            } else if (markedPoints.containsKey(index) && isError) {
                mPaint.color = Color.parseColor(colorArray[2])
            } else {
                mPaint.color = Color.parseColor(colorArray[0])
            }
            canvas?.drawCircle(mutableEntry.x, mutableEntry.y, mR.toFloat(), mPaint)
        }
    }

}