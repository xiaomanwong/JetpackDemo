package com.example.jetpackdemo.pacman

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.math.BigDecimal
import kotlin.math.abs
import kotlin.math.sqrt

class PacmanGame @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    var mWidth = 0
    var mHeight = 0
    private val broaderPath = Path()
    val SCALE = 1.0f

    var scaleYFloats = floatArrayOf(
        SCALE,
        SCALE / 2,
        SCALE / 4,
        SCALE / 2,
        SCALE
    )

    //
    var pacmanRadius = 30f

    var startRadio = 0f

    //
    val doudouRadius = 2f

    private var xCoordinate = 0f
    var yCoordinate = 0f

    var degrees1 = 0f

    var degrees2 = 0f

    val doudouPath = Path()

    val beanList = arrayListOf<Beans>()
    var intervalDistance: Float = 0f

    var isInit = false

    var isOnPacman = false
    private val mBoardPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mBeanPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPacmanPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var ref1 = RectF()


    var lastX = 0f
    var lastY = 0f

    init {
        mBoardPaint.color = Color.BLACK
        mBeanPaint.color = Color.YELLOW
        mPacmanPaint.color = Color.WHITE
        mBoardPaint.style = Paint.Style.STROKE
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mHeight = w
        mWidth = w

        intervalDistance = mWidth / 16f
        for (xIndex in 1..((mWidth / intervalDistance).toInt())) {
            for (yIndex in 1..((mWidth / intervalDistance).toInt())) {

                beanList.add(
                    Beans(
                        intervalDistance / 2 * (2 * xIndex - 1) - doudouRadius / 2,
                        intervalDistance / 2 * (2 * yIndex - 1) - doudouRadius / 2
                    )
                )
            }
        }
        initPath()

        ref1 = RectF(0f, 0f, intervalDistance, intervalDistance)

        broaderPath.lineTo(0f, mHeight.toFloat())
        broaderPath.lineTo(mWidth.toFloat(), mHeight.toFloat())
        broaderPath.lineTo(mWidth.toFloat(), 0f)
        broaderPath.lineTo(0f, 0f)
    }

    fun onCreateAnimators() {
        val delays = longArrayOf(100, 200, 300, 200, 100)
        for (i in 0..4) {
            val scaleAnim = ValueAnimator.ofFloat(1f, 0.2f, 1f)
            scaleAnim.duration = 1000
            scaleAnim.repeatCount = -1
            scaleAnim.startDelay = delays[i]
            scaleAnim.addUpdateListener { animation ->
                scaleYFloats[i] = animation.animatedValue as Float
                postInvalidate()
            }
            scaleAnim.start()
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_MOVE -> {
                if (!isOnPacman) {
                    return false
                }

                if (abs(x - lastX) > 20 || abs(y - lastY) > 20) {
                    lastY = event.y
                    lastX = event.x
                } else {
                    return false
                }
                // 开始滑动，重新计算 pacman 的位置，并调整方向
                reDrawPacman(event)
                post {
                    invalidate()
                }
                return true
            }
            MotionEvent.ACTION_DOWN -> {
                val x = event.x
                val y = event.y
                // 判断是否点击在 pacman 上
                (x >= ref1.left && x <= ref1.right && y >= ref1.top && y <= ref1.bottom).takeIf {
                    it
                }?.apply {
                    isOnPacman = true
                    lastX = event.x
                    lastY = event.y
                    println("点击在 pacman 上")
                }

                return true
            }
            MotionEvent.ACTION_UP -> {
                isOnPacman = false
                return true
            }
            else -> {

            }
        }

        return super.onTouchEvent(event)
    }

    private fun reDrawPacman(event: MotionEvent) {
        // 重新计算角度
        val x = BigDecimal(event.x.toDouble()).setScale(2, BigDecimal.ROUND_HALF_UP).toFloat()
        val y = BigDecimal(event.y.toDouble()).setScale(2, BigDecimal.ROUND_HALF_UP).toFloat()

        val k = (y - lastY) / (x - lastX)
        val radio = Math.atan(((y - lastY) / (x - lastX)).toDouble()).toFloat()

        if (k > 0) {

            startRadio += if (x > lastX) -radio else radio
//            startRadio -= if (x > lastX) {
//                -(radio - pacmanRadius)
//            } else {
//                pacmanRadius + 180
//            }

        } else {
            startRadio -= if (x > lastX) radio else radio + 180
//            startRadio += if (x > lastX) {
//                pacmanRadius
//            } else {
//                pacmanRadius + 180
//            }
        }

        println("开始角度： $startRadio")

        // 重新计算位置

        ref1.left = (x - sqrt(2.0) * intervalDistance / 3f).toFloat()
        ref1.top = (y - sqrt(2.0) * intervalDistance / 3f).toFloat()

        ref1.right = (x + sqrt(2.0) * intervalDistance / 3f).toFloat()
        ref1.bottom = (y + sqrt(2.0) * intervalDistance / 3f).toFloat()

        val bean = Beans(x, y)
        removeBean(bean)
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dp2px(dpValue: Int): Int {
        return (resources.displayMetrics.widthPixels * dpValue / 750)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawPath(broaderPath, mBoardPaint)


        canvas?.drawPath(doudouPath, mBeanPaint)
        canvas?.drawArc(
            ref1,
            startRadio + pacmanRadius,
            360f - 2 * pacmanRadius,
            true,
            mPacmanPaint
        )

        if (!isInit) {
            startPacmanAnimator()
        }
        isInit = true
    }

    private fun removeBean(bean: Beans) {
        val iterator = beanList.iterator()

        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item == bean) {
                iterator.remove()
                initPath()
                break
            }
        }
    }

    private fun initPath() {
        doudouPath.reset()
        for (item in beanList) {
            doudouPath.addCircle(item.x, item.y, doudouRadius, Path.Direction.CCW)
        }
    }

    private fun startPacmanAnimator() {
        // 张嘴动画
        val openMouseAnimator = ValueAnimator.ofFloat(pacmanRadius, 0f)
        openMouseAnimator.apply {
            repeatCount = -1
            repeatMode = ValueAnimator.REVERSE
            duration = 400
            addUpdateListener {
                pacmanRadius = it.animatedValue as Float
                invalidate()
            }
            start()
        }

    }
}

class Beans(var x: Float = 0f, var y: Float = 0f) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Beans

        if (x < other.x - 30 || x > other.x + 30) return false
        if (y < other.y - 30 || y > other.y + 30) return false

//
//        if (x != other.x) return false
//        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }
}