package com.example.myapplication.transition

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import androidx.transition.Transition
import androidx.transition.TransitionValues

/**
 * Create by ldr
 * on 2019/12/23 16:02.
 */
class ChangeColorTransition : Transition() {

    companion object {
        /**
         *  根据官网提供的命名规则 package_name:transition_class:property_name，避免跟与其他 TransitionValues 键起冲突
         *  将颜色值存储在TransitionValues对象中的键
         */
        private const val PROPNAME_BACKGROUND = "com.mzs.myapplication:transition_colors:background"
    }

    /**
     * 添加背景Drawable的属性值到目标的TransitionsValues.value映射
     */
    private fun captureValues(transitionValues: TransitionValues?) {
        val view = transitionValues?.view ?: return
        //保存背景的值，供后面使用
        transitionValues.values[PROPNAME_BACKGROUND] = (view.background as ColorDrawable).color
    }

    //关键方法一 ：捕获开始的场景值，多次调用
    override fun captureStartValues(transitionValues: TransitionValues) {
        if (transitionValues.view.background is ColorDrawable)
            captureValues(transitionValues)
    }

    //关键方法二 ：捕获结束的场景值，多次调用。
    // 将场景中的属性值存储到transitionValues的
    override fun captureEndValues(transitionValues: TransitionValues) {
        if (transitionValues.view.background is ColorDrawable)
            captureValues(transitionValues)
    }

    //关键方法三：根据
    override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {
        //存储一个方便的开始和结束参考目标。
        val view = endValues!!.view
        //存储对象包含背景属性为开始和结束布局
        var startBackground = startValues!!.values[PROPNAME_BACKGROUND]
        var endBackground = endValues!!.values[PROPNAME_BACKGROUND]
        //如果没有背景等的直接忽略掉
        if (startBackground != endBackground) {
            //定义属性动画。
            var animator = ValueAnimator.ofObject(ArgbEvaluator(), startBackground, endBackground)
            //设置监听更新属性
            animator.addUpdateListener { animation ->
                var value = animation?.animatedValue
                if (null != value) {
                    view.setBackgroundColor(value as Int)
                }
            }
            return animator
        }
        return null
    }
}
