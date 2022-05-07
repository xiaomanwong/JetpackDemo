package com.example.myapplication.transition

import android.animation.Animator
import android.animation.ValueAnimator
import android.transition.Transition
import android.transition.TransitionValues
import android.view.ViewGroup

/**
 * @author admin
 * @date 2022/4/29
 * @Desc
 */
class ChangeAlphaTransition(private val startValue: Float, private val endValue: Float) : Transition() {


    companion object {
        const val ALPHA_ANIMATOR = "zheli:transition:alpha"
    }


   private fun captureAlpha(transitionValues: TransitionValues) {
        val target = transitionValues.view
        transitionValues.values[ALPHA_ANIMATOR] = target.alpha
    }

    override fun captureStartValues(transitionValues: TransitionValues) {
        captureAlpha(transitionValues)
        transitionValues.values[ALPHA_ANIMATOR] = startValue
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        captureAlpha(transitionValues)
        transitionValues.values[ALPHA_ANIMATOR] = endValue
    }

    override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues,
        endValues: TransitionValues
    ): Animator? {
        val target = endValues.view

        val startValue = startValues.values[ALPHA_ANIMATOR]
        val endValue = endValues.values[ALPHA_ANIMATOR]

        if (startValue != endValue) {
           return  ValueAnimator.ofFloat(this.startValue, this.endValue) .apply {
                addUpdateListener {
                    target.alpha = it.animatedValue as Float
                }
            }
        }

        return null
    }
}