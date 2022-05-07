package com.example.myapplication.transition

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.PointF
import android.transition.Transition
import android.transition.TransitionValues
import android.util.Log
import android.view.ViewGroup

/**
 * @author admin
 * @date 2022/4/29
 * @Desc
 */
class ExitTransition(private val startRectF: Int, private val endRectF: Int) : Transition() {


    companion object {
        private const val PHOTO_WALL_SCALE = "com.yidian.zheli:transition_scale:layoutparamter"
    }

    private fun captureLayout(value: TransitionValues) {
        val target = value.view
//        val width = target.width
//        val height = target.height
//        val point = PointF(width.toFloat(), height.toFloat())
        value.values[PHOTO_WALL_SCALE] = target.scaleX
    }

    override fun captureStartValues(transitionValues: TransitionValues) {
        captureLayout(transitionValues)
        transitionValues.values[PHOTO_WALL_SCALE] = startRectF
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        captureLayout(transitionValues)
        transitionValues.values[PHOTO_WALL_SCALE] = endRectF
    }


    override fun createAnimator(
        sceneRoot: ViewGroup?,
        startValues: TransitionValues,
        endValues: TransitionValues
    ): Animator? {

        val startPoint: Int = startValues.values[PHOTO_WALL_SCALE] as Int
        val endPoint: Int = endValues.values[PHOTO_WALL_SCALE] as Int
        val view = endValues.view
        val matrix = view.matrix
        Log.d(
            "wangxu3",
            "ExitTransition ===> createAnimator() called with: sceneRoot = $sceneRoot, startValues = $startValues, endValues = $endValues"
        )

        if (startPoint != endPoint) {


            val ofInt = ValueAnimator.ofInt(startPoint, endPoint)
            ofInt.addUpdateListener {
                view.scaleX = (it.animatedValue as Int) .toFloat()
                view.scaleY =(it.animatedValue as Int) .toFloat()
            }
            return ofInt

//            ScaleAnimation(startPoint.x, endPoint.x ,startPoint.y, endPoint.y)
//            val animator =
////                ScaleAnimation(startPoint.x, endPoint.x ,startPoint.y, endPoint.y)
//                ValueAnimator.ofObject(PointFEvaluator(), startPoint, endPoint)
//            animator.addUpdateListener { animation ->
//                val value: PointF = animation.animatedValue as PointF
////                lp.width = value.x.toInt()
////                lp.height = value.y.toInt()
//                matrix.setScale(value.x, value.y)
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    view.animationMatrix =matrix
//                }
////                view.layoutParams = lp
//
//                Log.d(
//                    "wangxu3",
//                    "ExitTransition ===> createAnimator() called with: animation = $value"
//                )
//            }
//            return animator
        }
        return null
    }
}