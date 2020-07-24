package com.example.jetpackdemo.password

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.animation.addListener
import kotlin.math.hypot

/**
 * 手势密码
 */
class GesturePasswordView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var onCallback: CallBack? = null

    public fun setCallBack(callback: CallBack?): Unit {
        this.onCallback = callback
    }

    private var isError: Boolean = false
    private var isComplete = false
    private var mWidth = 0
    private var mHeight = 0
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val pointPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var animator: ValueAnimator? = null

    /**
     * 大圆的半径
     * 小圆的半径,是大圆的 1/4
     */
    private var mR = 0f
    private var r = 0f

    /**
     * 圆心距
     */
    private var mDistance = 0f

    /**
     * [0]: 灰色
     * [1]: 绿色
     * [2]: 红色
     */
    private val colorArray = arrayListOf("#C3C5D2", "#28BC7C", "#D14646")
    private val markPoints = LinkedHashMap<Int, PointF>(9)
    private val pointsMap = HashMap<Int, PointF>(9)
    private val lastPointF = PointF()
    private val currentPointF = PointF()

    private val lintPath: Path = Path()
    private val movePath = Path()

    init {
        circlePaint.color = Color.parseColor(colorArray[0])
        circlePaint.strokeWidth = 2f
        circlePaint.style = Paint.Style.STROKE
        pointPaint.color = Color.parseColor(colorArray[0])
        linePaint.color = Color.parseColor(colorArray[1])
        linePaint.style = Paint.Style.STROKE
        isClickable = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val width: Int
        val height: Int
        // 将页面设置为一个固定的正方形
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            width = widthSize
            height = widthSize
        } else {
            // 自适应状态下，获取屏幕的宽高
            // 保证为正方形，则将高度设置为和宽度一样
            width = resources.displayMetrics.widthPixels
            height = width
        }
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 保存宽高值
        mWidth = w
        mHeight = h
        mR = (mWidth - paddingStart - paddingEnd) / 9f
        r = mR / 4f
        mDistance = mR * 3
        linePaint.strokeWidth = r / 3
        createGraph()
    }

    /**
     * 创建视图内容，并记录所有点及索引
     */
    private fun createGraph(): Unit {
        var centerX: Float
        var centerY: Float

        var index = 0
        // 画行
        for (row in 0..2) {
            // 画列
            for (column in 0..2) {
                centerX = paddingStart + mR / 2 + mR + mDistance * column
                centerY = paddingTop + mR / 2 + mR + mDistance * row
                if (pointsMap[index] == null) {
                    pointsMap[index] = PointF(centerX, centerY)
                }
                index++
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        pointsMap.forEach {
            val index = it.key
            val pointF = it.value
            if (markPoints.contains(index) && !isError) {
                circlePaint.color = Color.parseColor(colorArray[1])
                pointPaint.color = Color.parseColor(colorArray[1])
                linePaint.color = Color.parseColor(colorArray[1])
            } else if (markPoints.contains(index) && isError) {
                circlePaint.color = Color.parseColor(colorArray[2])
                pointPaint.color = Color.parseColor(colorArray[2])
                linePaint.color = Color.parseColor(colorArray[2])
            } else {
                circlePaint.color = Color.parseColor(colorArray[0])
                pointPaint.color = Color.parseColor(colorArray[0])
            }
            canvas?.drawCircle(pointF.x, pointF.y, mR, circlePaint)
            canvas?.drawCircle(pointF.x, pointF.y, r, pointPaint)
        }

        // 画连线
        markPoints.entries.forEachIndexed { index, mutableEntry ->
            val pointF = mutableEntry.value
            if (index == 0) {
                lintPath.moveTo(pointF.x, pointF.y)
            } else {
                lintPath.lineTo(pointF.x, pointF.y)
            }
        }
        canvas?.drawPath(lintPath, linePaint)
        if (!isError) {
            canvas?.drawPath(movePath, linePaint)
        }
    }

    /**
     * 触摸事件
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isComplete) {
            return false
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                // down 记录坐标
                // move 时，先校验是否已经记录坐标，并计算是否在 point 上
                onDownMove(event)
            }
            MotionEvent.ACTION_UP -> {
                movePath.reset()
                completed()
                invalidate()
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 按压和滑动事件
     * 1. Obtain the coordinate which on down or move event, make the coordinate in View
     * 2. Search the point where in pointMap, get the key that index in View, and put into markedPoints, call the onPasswordChanged with index
     * 3. draw the point with green Paint,and connect all the points
     */
    private fun onDownMove(event: MotionEvent): Boolean {
        var downX = event.x
        var downY = event.y

        /*
            如果横坐标大于右边界, 则设置横坐标为右边界 - 1 , -1 是为了防止取整时,数据大于可控范围,故将数据缩小,并向下取整
            如果横坐标小于左边界, 则设置横坐标为左边界

            如果纵坐标大于下边界, 则设置纵坐标为下边界 - 1 , -1 是为了防止取整时,数据大于可控范围,故将数据缩小,并向下取整
            如果纵坐标小于上边界, 则设置纵坐标为上边界
         */
        if (downX >= mWidth - paddingEnd) {
            downX = mWidth.toFloat() - paddingEnd - 1
        }

        if (downX <= paddingStart) {
            downX = paddingStart.toFloat()
        }

        if (downY >= mHeight - paddingBottom) {
            downY = mHeight.toFloat() - paddingBottom - 1
        }

        if (downY <= paddingTop) {
            downY = paddingTop.toFloat()
        }

        // 寻找当前点所在的矩形框,并获取到矩形框内圆的中心坐标
        val columnIndex = (downX - paddingStart) / (3 * mR)
        val rowIndex = (downY - paddingTop) / (3 * mR)
        val index = 3 * rowIndex.toInt() + columnIndex.toInt()
        // 控制数据,防止发生意外,但实际没什么用
        if (index > 8) return false
        println("index = $index column= $columnIndex rowIndex = $rowIndex")
        val pointF = pointsMap[index]!!
        // 以矩形框内的圆建立直角坐标系,计算当前点,是否在圆内
        val circleX = downX - pointF.x
        val circleY = downY - pointF.y
        currentPointF.set(downX, downY)
        // 在圆内且未被标记的点,则将点添加为标记状态,同时更新 view, 回调前台被选中的坐标索引
        if (hypot(circleX, circleY) < mR && !markPoints.containsKey(index)) {
            lastPointF.set(pointF.x, pointF.y)
            markPoints[index] = pointF
            this.onCallback?.onPasswordChanged(index)
        }
        // 每次重置移动路径,用来绘制最后一个点和当前点的连接线
        movePath.reset()
        if (markPoints.size > 0) {
            movePath.moveTo(lastPointF.x, lastPointF.y)
            movePath.lineTo(currentPointF.x, currentPointF.y)
        }
        invalidate()
        return true
    }

    /**
     * 完成后，回调，用来通知密码结果
     */
    private fun completed(): Unit {
        var password = ""
        markPoints.forEach() {
            password += it.key
        }
        println("password = $password")
        this.onCallback?.onFinish(password)
        isComplete = true
    }

    /**
     * Reset the path to restore View to its original state
     */
    private fun resetPath(): Unit {
        markPoints.clear()
        lintPath.reset()
        movePath.reset()
    }


    /**
     * 设置结果
     * @param isError
     *
     * @description The true means the password is correct else it is wrong
     *
     * true: To make view shake what 5 times, and the paint color is red [colorArray]
     * false: the markedPoints will be clean,and redraw the view
     */
    fun setResult(isError: Boolean): Unit {
        this.isError = isError
        if (isError) {
            startShakeAnimator()
        } else {
            resetPath()
        }
        invalidate()
    }

    /**
     * start to shake self what 5 times, then onEnd() to call onError() which in CallBack interface and reset the paint color to red
     */
    private fun startShakeAnimator(): Unit {
        animator = ValueAnimator.ofFloat(15f, -15f, 15f).apply {
            repeatCount = 5
            duration = 100
            addUpdateListener {
                x = it.animatedValue as Float
            }
            addListener(onEnd = {
                isError = false
                isComplete = false
                resetPath()
                onCallback?.onError()
                invalidate()
            })
            start()
        }
    }

    override fun onDetachedFromWindow() {
        animator?.cancel()
        animator?.removeAllListeners()
        super.onDetachedFromWindow()
    }

    interface CallBack {
        // 用来绘制小图案， password 是增量
        fun onPasswordChanged(password: Int)

        // 用来接收最后结果
        fun onFinish(password: String)

        // 错误回调
        fun onError()
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private fun dp2px(dpValue: Float): Int {
        return (resources.displayMetrics.widthPixels * dpValue / 750).toInt()
    }
}