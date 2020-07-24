package com.example.jetpackdemo.table

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class CharListView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // 控件宽高
    var mWidth: Int = 0
    var mHeight: Int = 0

    // 红色圈半径
    var remindR: Float = 10f

    var blockWidth: Float = 0f


    // 文字宽高
    var textWidth: Int = 0
    var remindWidth: Int = 0
    var textHeight: Int = 0
    var drawStartH: Int = 0

    var touchIndex = -1

    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPaintRemind: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPaintRemindText: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPaintGreen = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        mPaint.color = Color.GRAY
        mPaint.textAlign = Paint.Align.CENTER
        this.setPadding(left, 40 + top, right, 40 + bottom)
        mPaintRemind.color = Color.RED
        mPaintGreen.color = Color.GREEN
        mPaintRemind.textAlign = Paint.Align.CENTER
        mPaintRemindText.color = Color.BLACK
        mPaintRemindText.textAlign = Paint.Align.CENTER
        isClickable = true
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, 20 + top, right, 20 + bottom)
    }

    val array = arrayOf(
        "#", "A", "B", "C", "D", "E",
        "F", "G", "H", "I", "J", "K",
        "L", "M", "N", "O", "P", "Q",
        "R", "S", "T", "U", "V", "W",
        "X", "Y", "Z"
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        var width = 0
        var height = 0
        when (widthMode) {
            // match_parent 或 精确尺寸，则直接使用
            MeasureSpec.EXACTLY -> width = widthSize
            // wrap_content 等不确定尺寸，可以规定尺寸
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED -> {
                println("测量 $textWidth")
                width = (textWidth + blockWidth + 2 * remindR).toInt()
            }
        }

        when (heightMode) {
            MeasureSpec.EXACTLY -> mHeight = heightSize
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED -> {
                height = resources.displayMetrics.heightPixels / 2
            }
        }

        println("width = $width height = $height")
        setMeasuredDimension(width, height)
//        mHeight = h - paddingTop - paddingBottom
//        mWidth = w
        mHeight = height - paddingTop - paddingBottom
        mWidth = width
        // 计算实际内容大小
        textHeight = mHeight / array.size
        mPaint.textSize = textHeight.toFloat()
        mPaintRemind.textSize = textHeight.toFloat()
        textWidth = mPaint.measureText("M").toInt()
        println("测量结束 $textWidth")
        // 间隔
        blockWidth = textWidth.toFloat()
        // 计算 remind 画笔属性
        mPaintRemindText.textSize = 2f * textWidth.toFloat()
        remindWidth = mPaintRemindText.measureText("M").toInt()
        remindR = remindWidth.toFloat()
        drawStartH = (mHeight - textHeight * array.size) / 2
        println("textHeight = $textHeight , drawStartH = $drawStartH")
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
//        mHeight = h - paddingTop - paddingBottom
//        mWidth = w
        println("$w######################$h")
//        mHeight = height - paddingTop - paddingBottom
//        mWidth = width
        // 计算实际内容大小
//        textHeight = mHeight / array.size
//        mPaint.textSize = textHeight.toFloat()
//        mPaintRemind.textSize = textHeight.toFloat()
//        textWidth = mPaint.measureText("M").toInt()
//        println("测量结束 $textWidth")
//        // 间隔
//        blockWidth = textWidth.toFloat()
//        // 计算 remind 画笔属性
//        mPaintRemindText.textSize = 2f * textWidth.toFloat()
//        remindWidth = mPaintRemindText.measureText("M").toInt()
//        remindR =  remindWidth.toFloat()
//        drawStartH = (mHeight - textHeight * array.size) / 2
//        println("textHeight = $textHeight , drawStartH = $drawStartH")
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_DOWN -> {
                // 判断是否在文字区域
                val x = event.x
                val y = event.y
                ((x > mWidth - textWidth - -paddingLeft - paddingTop)
                        && (y < mHeight + paddingBottom)).takeIf {
                    if (it) {
                        touchIndex = +((y - drawStartH - paddingTop) / (textHeight)).toInt()
                        invalidate()
                    }
                    return it
                }
            }
            MotionEvent.ACTION_UP -> {
                touchIndex = -1
                invalidate()
            }
        }

        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var index = 0
        array.forEach {
            if (index == this.touchIndex) {
                canvas?.drawCircle(
                    mWidth - textWidth - blockWidth - remindR,
                    paddingTop + (index * textHeight + drawStartH + textWidth).toFloat() - textHeight / 2,
                    remindR,
                    mPaintRemind
                )

                canvas?.drawText(
                    it,
                    mWidth - textWidth - blockWidth - remindR,
                    paddingTop + (index * textHeight + drawStartH + textWidth).toFloat(),
                    mPaintRemindText
                )
            }
            canvas?.drawText(
                it,
                (mWidth - textWidth).toFloat(),
                paddingTop + (index * textHeight + drawStartH + textWidth).toFloat(),
                mPaint
//                if (index == this.touchIndex) mPaintRemindText else mPaint
            )
            index++
        }
    }
}