package com.example.myapplication.custom_page

/**
 * @author Quinn
 * @date 2024/11/18
 * @Desc
 */
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.log
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class UserOnlineStatusHeaderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var isOnline: Boolean = false
    private val onlineIndicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        setShadowLayer(1f, 0f, 0f, Color.WHITE)
    }

    private val onlineInnerIndicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GREEN
        style = Paint.Style.FILL
        setShadowLayer(1f, 0f, 0f, Color.WHITE)
    }
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = dp2px(2f).toFloat()
    }

    private val headerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    var headerProfile: Bitmap? = null
        set(value) {
            field = value
            invalidate()
        }

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    fun setOnlineStatus(isOnline: Boolean) {
        this.isOnline = isOnline
        invalidate()
    }

    private val avatarPath = Path()

    private var onlineOuterRadius = 0f
    private var onlineInnerRadius = 0f

    private fun dp2px(dpValue: Float): Int {
        return (resources.displayMetrics.widthPixels * dpValue / 750).toInt()
    }

    fun pxToDp(context: Context, px: Int): Int {
        val density = context.resources.displayMetrics.density
        return (px / density).roundToInt()
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
        val size = min(parentWidth, parentHeight)
        val radius = size / 2f
        // Draw the circular avatar
        avatarPath.reset()
        avatarPath.addCircle(size / 2f, size / 2f, radius, Path.Direction.CW)
        onlineOuterRadius = max(dp2px(10f), (radius / 4).toInt()).toFloat()
        onlineInnerRadius = onlineOuterRadius / 2f + dp2px(3f)
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.clipPath(avatarPath)
        headerProfile?.let {
            canvas.drawBitmap(it, 0f, 0f, headerPaint)
        }
        canvas.restore()
        // Draw the online indicator
        if (isOnline) {
            println("isOnline $onlineInnerRadius $onlineOuterRadius ${dp2px(2.5f)}")
            val cx = width - onlineOuterRadius
            val cy = height - onlineOuterRadius
            canvas.drawCircle(cx, cy, onlineOuterRadius, onlineIndicatorPaint)
            canvas.drawCircle(cx, cy, onlineInnerRadius, onlineInnerIndicatorPaint)
        }

    }

}