package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.NonNull
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetDialogTest : BottomSheetDialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        val dialog = CustomBottomSheetDialog(requireContext())
        val boxView: View = LayoutInflater.from(context).inflate(R.layout.dialog_bottom_sheet_fragment_test, null)
//        dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) //←重点在这里，来，都记下笔记
        dialog.window!!.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.window!!.statusBarColor = Color.TRANSPARENT
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(boxView)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

        

        return dialog
    }


    override fun onStart() {
        super.onStart()

        val dialog = dialog;
        dialog?.let {
            val bottomSheet: View = dialog.findViewById(R.id.design_bottom_sheet)
            bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        }

        val view: View? = view
        view?.let {
            it.post {
                val parent: View = it.parent as View

                val params: CoordinatorLayout.LayoutParams = parent.layoutParams as CoordinatorLayout.LayoutParams

                val behavior: CoordinatorLayout.Behavior<*> = params.behavior as CoordinatorLayout.Behavior
//                (behavior as BottomSheetBehavior<View>).peekHeight = view.measuredHeight
                parent.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }
}


class CustomBottomSheetDialog : BottomSheetDialog {

    constructor(@NonNull context: Context) : super(context) {

    }

    constructor(@NonNull context: Context, @StyleRes theme: Int) : super(context, theme) {

    }

    constructor(@NonNull context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(
        context,
        cancelable,
        cancelListener
    ) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val screenHeight = getScreenHeight(context)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, if (screenHeight == 0) ViewGroup.LayoutParams.MATCH_PARENT else screenHeight)
    }

    private fun getScreenHeight(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return wm.defaultDisplay.height
    }
}