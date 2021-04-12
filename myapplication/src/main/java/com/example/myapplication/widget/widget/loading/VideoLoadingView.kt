package com.example.myapplication.widget.widget.loading

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View


class VideoLoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    //    private var animator: ValueAnimator? = null
    private var isStartAnimator: Boolean = false
    var intervalSpace: Int = 0

    private val SCALE = 1.0F
    private val radius = 10F

    private val SCALE_ARRAY = floatArrayOf(SCALE, SCALE, SCALE)
    private val SCALE_DELAY = longArrayOf(300, 600, 900)
    private val mPath: Path
    private val corEffect: CornerPathEffect

    private var mPaint: ArrayList<Paint>? = null
    private val orangePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val greenPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bluePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val animatorList: ArrayList<ValueAnimator> = arrayListOf()

    init {
        orangePaint.strokeCap = Paint.Cap.ROUND
        orangePaint.color = Color.RED
        orangePaint.strokeWidth = 5f

        greenPaint.strokeCap = Paint.Cap.ROUND
        greenPaint.color = Color.GREEN
        greenPaint.strokeWidth = 5f

        bluePaint.strokeCap = Paint.Cap.ROUND
        bluePaint.color = Color.BLUE
        bluePaint.strokeWidth = 5f
        mPaint = arrayListOf(orangePaint, greenPaint, bluePaint)
        corEffect = CornerPathEffect(radius)
        mPath = Path()
        mPath.moveTo(dp2px(5), dp2px(5))
        mPath.lineTo(dp2px(25), dp2px(5))
        mPath.lineTo(dp2px(20), dp2px(25))
        mPath.lineTo(dp2px(0), dp2px(25))
        mPath.close()
        setPadding(dp2px(5).toInt(), dp2px(5).toInt(), dp2px(5).toInt(), dp2px(5).toInt())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = dp2px(100)
        val height = dp2px(25)
        intervalSpace = width.toInt() / 5 - 5
        setMeasuredDimension(width.toInt(), height.toInt())
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (index in 0..2) {
            canvas?.save()
            mPaint?.get(index)!!.pathEffect = corEffect
            canvas?.translate(2 * (intervalSpace * index).toFloat(), 0f)
            canvas?.scale(SCALE, SCALE_ARRAY[index], width.toFloat(), height / 2f)
            canvas!!.drawPath(mPath, mPaint?.get(index)!!)
            canvas.restore()
        }
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == VISIBLE) {
            startAnimator()
        } else {
            cancelAnimator()
        }
    }

    private fun cancelAnimator() {
        animatorList.forEach {
            it.cancel()
            it.removeAllUpdateListeners()
        }
        isStartAnimator = false
    }

    /**
     * 执行缩放延时动画
     */
    private fun startAnimator() {
        if (isStartAnimator) {
            return
        }

        for (index in SCALE_DELAY.indices) {
            val animator = ValueAnimator.ofFloat(1.0f, 0f, 1.0f)
            animator?.apply {
                duration = 1000
                repeatCount = -1
                startDelay = SCALE_DELAY[index]
                addUpdateListener {
                    SCALE_ARRAY[index] = it.animatedValue as Float
                    invalidate()
                }
                start()
            }
            animatorList.add(animator)
        }
        isStartAnimator = true
    }


    override fun onDetachedFromWindow() {
        animatorList.forEach {
            it.cancel()
            it.removeAllUpdateListeners()
        }
        super.onDetachedFromWindow()
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private fun dp2px(dpValue: Int): Float {
        return (resources.displayMetrics.widthPixels * dpValue / 750).toFloat()
    }

}