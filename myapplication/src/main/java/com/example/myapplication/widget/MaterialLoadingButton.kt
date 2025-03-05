/**
 * A custom LinearLayout that combines a TextView and a ProgressBar to create a loading button.
 * The button can switch between a loading state and a normal state with a smooth transition.
 *
 * @constructor Creates a MaterialLoadingButton with the specified context and attribute set.
 * @param context The Context the view is running in, through which it can access the current theme, resources, etc.
 * @param attrs The attributes of the XML tag that is inflating the view.
 *
 * @property progressBar The ProgressBar that indicates loading state.
 * @property button The TextView that represents the button text.
 *
 * @init Initializes the MaterialLoadingButton by setting its orientation, gravity, and attributes.
 * It also configures the ProgressBar and TextView based on the custom attributes defined in XML.
 *
 * @method setEnabled Enables or disables the button with a transition animation.
 * When disabled, the ProgressBar is shown and the TextView is hidden.
 * When enabled, the TextView is shown and the ProgressBar is hidden.
 * @param enabled A boolean indicating whether the button should be enabled or disabled.
 *
 * @method setButtonText Sets the text of the button.
 * @param text The text to be displayed on the button.
 */
package com.example.myapplication.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import com.example.myapplication.R

/**
 * Author: Quinn
 * Date: 2024/9/27
 * Description:
 */
class MaterialLoadingButton(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs, 0) {

    private val progressBar: ProgressBar
    private val button: TextView
    private var type: String = "TEXT"

    init {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER

        progressBar = ProgressBar(context).apply {
            visibility = GONE
        }
        button = TextView(context)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialLoadingButton)
        val progressBarColor =
            typedArray.getColor(R.styleable.MaterialLoadingButton_progressBarColor, 0)
        val buttonTextColor =
            typedArray.getColor(R.styleable.MaterialLoadingButton_buttonTextColor, 0)
        val buttonTextSize =
            typedArray.getDimension(R.styleable.MaterialLoadingButton_buttonTextSize, 14f)

        val progressBarSize =
            typedArray.getDimension(R.styleable.MaterialLoadingButton_progressBarSize, 20f)
        val buttonTextStyle =
            typedArray.getInt(R.styleable.MaterialLoadingButton_buttonTextStyle, 0)
        typedArray.recycle()

        progressBar.indeterminateDrawable.setColorFilter(
            progressBarColor,
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        button.setTextColor(buttonTextColor)
        button.textSize = buttonTextSize
        button.setTypeface(null, buttonTextStyle)

        addView(progressBar)
        addView(button)
        progressBar.updateLayoutParams {
            width = progressBarSize.toInt()
            height = progressBarSize.toInt()
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
//        if (type == "TEXT") {
            super.setOnClickListener(l)
//        }
    }

    fun toggle() {
//        val transition = android.transition.AutoTransition().apply {
//            duration = 200 // Set animation duration
//        }
//        android.transition.TransitionManager.beginDelayedTransition(this, transition)

        if (type != "TEXT") {
            button.visibility = VISIBLE
            progressBar.visibility = GONE
            type = "PROGRESS_BAR"
        } else {
            progressBar.visibility = VISIBLE
            button.visibility = GONE
            type = "TEXT"
        }
        invalidate()
    }


    fun setButtonText(text: String) {
        button.text = text
    }
}
