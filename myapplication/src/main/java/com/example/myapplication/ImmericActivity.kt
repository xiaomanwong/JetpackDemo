package com.example.myapplication

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.example.lib_annotation.ActivityDestination
import com.example.lib_annotation.FragmentDestination
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback


@ActivityDestination(pageUrl = "main/ImmericActivity", asStart = true)
class ImmericActivity : AppCompatActivity() {


    var textView: TextView? = null

    var cancelable = true
    private var container: FrameLayout? = null

    private var canceledOnTouchOutside = true
    private var canceledOnTouchOutsideSet = false


    var dismissWithAnimation = false
    private var behavior: BottomSheetBehavior<FrameLayout>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme_FullScreen)
        setContentView(wrapInBottomSheet(R.layout.activity_immeric, null, null))
        container?.fitsSystemWindows = false
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        supportActionBar?.hide()
        actionBar?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        actionBar?.hide()
        textView = findViewById(R.id.tv_text)
        textView?.setOnClickListener {
            BottomSheetDialogTest().show(supportFragmentManager, "")
        }
    }

    private fun wrapInBottomSheet(
        layoutResId: Int, view: View?, params: ViewGroup.LayoutParams?
    ): View? {
        var view = view
        ensureContainerAndBehavior()
        val coordinator = container!!.findViewById<View>(R.id.coordinator) as CoordinatorLayout
        if (layoutResId != 0 && view == null) {
            view = layoutInflater.inflate(layoutResId, coordinator, false)
        }
        val bottomSheet = container!!.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout
        bottomSheet.removeAllViews()
        if (params == null) {
            bottomSheet.addView(view)
        } else {
            bottomSheet.addView(view, params)
        }
        // We treat the CoordinatorLayout as outside the dialog though it is technically inside
        coordinator
            .findViewById<View>(R.id.touch_outside)
            .setOnClickListener {
                if (cancelable && shouldWindowCloseOnTouchOutside()) {
                    cancel()
                }
            }
        // Handle accessibility events
        ViewCompat.setAccessibilityDelegate(
            bottomSheet,
            object : AccessibilityDelegateCompat() {
                override fun onInitializeAccessibilityNodeInfo(
                    host: View, info: AccessibilityNodeInfoCompat
                ) {
                    super.onInitializeAccessibilityNodeInfo(host, info)
                    if (cancelable) {
                        info.addAction(AccessibilityNodeInfoCompat.ACTION_DISMISS)
                        info.isDismissable = true
                    } else {
                        info.isDismissable = false
                    }
                }


                override fun performAccessibilityAction(host: View, action: Int, args: Bundle?): Boolean {
                    if (action == AccessibilityNodeInfoCompat.ACTION_DISMISS && cancelable) {
                        cancel()
                        return true
                    }
                    return super.performAccessibilityAction(host, action, args)
                }
            })
        bottomSheet.setOnTouchListener { view, event -> // Consume the event and prevent it from falling through
            true
        }
        return container
    }

    private fun shouldWindowCloseOnTouchOutside(): Boolean {
        if (!canceledOnTouchOutsideSet) {
//            val a: TypedArray = getContext().obtainStyledAttributes(intArrayOf(android.R.attr.windowCloseOnTouchOutside))
//            canceledOnTouchOutside = a.getBoolean(0, true)
//            a.recycle()
            canceledOnTouchOutsideSet = true
        }
        return canceledOnTouchOutside
    }

    override fun onStart() {
        super.onStart()
        if (behavior != null && behavior!!.state == BottomSheetBehavior.STATE_HIDDEN) {
            behavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
            window.decorView.setBackgroundColor(Color.TRANSPARENT)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            window.setBackgroundDrawableResource(R.drawable.sdfsaf)
        }
    }


    fun getBehavior(): BottomSheetBehavior<FrameLayout>? {
        if (behavior == null) {
            // The content hasn't been set, so the behavior doesn't exist yet. Let's create it.
            ensureContainerAndBehavior()
        }
        return behavior
    }

    /** Creates the container layout which must exist to find the behavior  */
    private fun ensureContainerAndBehavior(): FrameLayout? {
        if (container == null) {
            container = View.inflate(this, R.layout.design_bottom_sheet_dialog2, null) as FrameLayout
            val bottomSheet = container?.findViewById(R.id.design_bottom_sheet) as FrameLayout
            behavior = BottomSheetBehavior.from(bottomSheet)
            behavior?.addBottomSheetCallback(bottomSheetCallback)
            behavior?.isHideable = cancelable
        }
        return container
    }

    fun cancel() {
        val behavior = getBehavior()
        if (!dismissWithAnimation || behavior!!.state == BottomSheetBehavior.STATE_HIDDEN) {
            finish()
//            super.cancel()
        } else {
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN)
        }
    }

    private val bottomSheetCallback: BottomSheetCallback = object : BottomSheetCallback() {
        override fun onStateChanged(
            bottomSheet: View, @BottomSheetBehavior.State newState: Int
        ) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                cancel()
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

}