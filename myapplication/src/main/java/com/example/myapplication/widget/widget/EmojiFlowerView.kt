package com.example.myapplication.widget.widget

/**
 * @author Quinn
 * @date 2024/9/20
 * @Desc
 */
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import kotlin.math.atan2

class EmojiFireworksView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var emoji = "\uD83C\uDF89" // 默认礼花 emoji
    private var emojiSize = 50f
    private var animators: MutableList<ValueAnimator> = mutableListOf()
    private val paints: MutableList<Paint> = MutableList(6) {
        Paint().apply {
            color = Color.BLACK
            textSize = emojiSize
            isAntiAlias = true
            alpha = 178
        }
    }

    private val positions = Array(6) { PointF(0f, 0f) }
    private val tangents = Array(6) { FloatArray(2) }
    private var paths: MutableList<Path> = mutableListOf()
    private val handler = Handler(Looper.getMainLooper())

    init {
        setupAnimations()
        background = ColorDrawable(Color.GREEN)
    }

    // 通过参数设置 emoji 字符，并开始动画
    fun setEmoji(newEmoji: String) {
        this.emoji = newEmoji
        startAnimations() // 在设置新 emoji 时启动动画
    }

    private fun setupAnimations() {
        // 初始化动画但不设置路径，路径会在 onSizeChanged 中计算
        for (i in 0 until 6) {
            positions[i] = PointF(0f, 0f) // 初始化位置

            val animator = ValueAnimator.ofFloat(0f, 1f).apply {
                duration = 666 * 2
                repeatCount = 3 // 循环次数
                addUpdateListener { animation ->
                    val pathMeasure = PathMeasure(paths[i], false)
                    val value = animation.animatedValue as Float
                    val pos = FloatArray(2)
                    val tan = FloatArray(2)
                    pathMeasure.getPosTan(pathMeasure.length * value, pos, tan)

                    // 更新 emoji 的位置
                    positions[i].set(pos[0], pos[1])
                    tangents[i] = tan // 记录切线方向

                    // 检查动画进度，透明度变化在最后 200ms（大约最后 1/15 的时间）
                    val fraction = animation.animatedFraction
                    if (fraction > 0.3f) { // 透明度从 0.3 开始变化
                        val alphaValue = ((1f - (fraction - 0.3f) / 0.7f) * 255).toInt()
                        paints[i].alpha = alphaValue
                    } else {
                        paints[i].alpha = 255 // 其他时间透明度为 255
                    }

                    // 重新绘制 view
                    invalidate()
                }
            }

            animators.add(animator)
        }
    }

    private fun startAnimations() {
        // 确保每次设置 emoji 后重新启动动画
        if (paths.isNotEmpty()) {
            // 随机执行顺序
            val randomOrder = (0 until paths.size).toList().shuffled()

            // 逐个启动动画，每个动画延迟 10ms 启动
            randomOrder.forEachIndexed { index, animatorIndex ->
                handler.postDelayed({
                    animators[animatorIndex].start() // 延迟启动每个动画
                }, index * 300L) // 每个动画间隔 10 毫秒
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val widthCenter = w / 2f
        val heightThreeQuarter = h * 4 / 5f
        val heightBottom = h.toFloat()
        paths.clear()

        // 第一条贝塞尔曲线
        val path1 = Path().apply {
            moveTo(widthCenter, heightThreeQuarter)
            quadTo(w * 0.17f, -heightBottom * 0.8f, 0f, heightBottom * 1.4f)
        }

        // 第二条贝塞尔曲线
        val path2 = Path().apply {
            moveTo(widthCenter, heightThreeQuarter)
            quadTo(w * 0.45f, heightThreeQuarter * 0.1f, w * 0.45f, heightBottom)
        }

//        // 第三条贝塞尔曲线
        val path3 = Path().apply {
            moveTo(widthCenter, heightThreeQuarter)
            quadTo(w * 0.17f, -heightBottom * 0.2f, 0f, heightBottom * 1.4f)
        }
//
//        // 第四条贝塞尔曲线
        val path4 = Path().apply {
            moveTo(widthCenter, heightThreeQuarter)
            quadTo(w * 0.83f, -heightBottom * 0.2f, w.toFloat(), heightBottom * 1.4f)
        }

        val path5 = Path().apply {
            moveTo(widthCenter, heightThreeQuarter)
            quadTo(w * 0.83f, -heightBottom * 0.2f, w.toFloat(), heightBottom * 1.4f)
        }

        val path6 = Path().apply {
            moveTo(widthCenter, heightThreeQuarter)
            quadTo(w * 0.55f, -heightBottom * 0.2f, w.toFloat(), heightBottom * 1.4f)
        }

        paths.addAll(listOf(path1, path2, path3, path4, path5, path6))

        // 只在动画路径准备好后启动动画
        startAnimations()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 绘制所有的 emoji，增加旋转效果
        positions.forEachIndexed { index, position ->
            // 保存 canvas 状态
            canvas.save()

            // 获取当前切线方向
            val tan = tangents[index]
            val angle = (atan2(tan[1], tan[0]) * 180 / Math.PI).toFloat() + 95f // 加 95 度旋转

            // 移动到 emoji 的绘制位置并旋转
            canvas.translate(position.x, position.y)
            canvas.rotate(angle)

            // 绘制 emoji
            canvas.drawText(emoji, 0f, 0f, paints[index])

            // 恢复 canvas 状态
            canvas.restore()
        }
    }
}