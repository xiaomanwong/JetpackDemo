package com.example.myapplication.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.DialogBaseSystemListBinding
import com.example.myapplication.util.post

/**
 * @author Quinn
 * @date 2023/7/10
 * @Desc
 */
class SystemListDialog private constructor() : DialogFragment() {

    lateinit var builder: Builder

    constructor(builder: Builder) : this() {
        this.builder = builder
    }

    private val binding: DialogBaseSystemListBinding by lazy {
        DialogBaseSystemListBinding.inflate(layoutInflater)
    }

    private val adapter by lazy {
        ItemAdapter()
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.window?.apply {
            val attr = attributes
            attr.gravity = builder.gravity
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attr.width = WindowManager.LayoutParams.MATCH_PARENT
            setWindowAnimations(R.style.DialogAnimator)
            attributes = attr
//            this.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
//            attributes?.windowAnimations = R.style.DialogFragmentFromBottomAnimation
        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        builder.apply {

            dialog?.setCanceledOnTouchOutside(builder.outsideCancel)

            binding.content.adapter = adapter
            adapter.itemClick = builder.itemClick
            adapter.content = builder.content

        }

    }

    override fun onStart() {
        super.onStart()
        binding.root.post{
            dialog?.window?.apply {
                val attr = attributes

                attr.width = WindowManager.LayoutParams.MATCH_PARENT
                attributes = attr
            }
        }
    }

    companion object {
        class Builder {
            var content: List<String> = listOf()
            var visibleClose: Boolean = false
            var itemClick: OnListDialogItemClick? = null
            var outsideCancel: Boolean = false

            var gravity: Int = Gravity.BOTTOM
            fun build(): SystemListDialog {

                check(content.isNotEmpty()) {
                    "content is empty ,please set content before build"
                }

                return SystemListDialog(this)
            }
        }
    }

    fun dp2px(dpValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue,
            resources.displayMetrics
        ).toInt()
    }

    inner class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

        var content = listOf<String>()

        var itemClick: OnListDialogItemClick? = null

        inner class ItemViewHolder(val childView: View) : RecyclerView.ViewHolder(childView) {
            fun bind(item: String) {
                (childView as? TextView)?.text = item
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ItemAdapter.ItemViewHolder {
            val item = TextView(context)
            context?.let {
                item.background =
                    AppCompatResources.getDrawable(it, R.drawable.dialog_sheet_bg_white_r12)
                item.layoutParams = MarginLayoutParams(
                    MarginLayoutParams.MATCH_PARENT,
                    dp2px(51f)
                )
                item.updateLayoutParams<MarginLayoutParams> {
                    marginStart = dp2px(12f)
                    marginEnd = dp2px(12f)
                    bottomMargin = dp2px(7f)
                }

                item.setTypeface(null, Typeface.BOLD)
                item.gravity = Gravity.CENTER
                item.setTextColor(Color.parseColor("#000000"))
                item.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
            }
            return ItemViewHolder(item)

        }

        override fun onBindViewHolder(holder: ItemAdapter.ItemViewHolder, position: Int) {
            holder.bind(content.getOrElse(position) { "" })
            holder.itemView.setOnClickListener {
                itemClick?.invoke(
                    this@SystemListDialog,
                    position,
                    content.getOrNull(position) ?: ""
                )
            }
        }

        override fun getItemCount(): Int {
            return content.size
        }
    }
}

typealias OnListDialogItemClick = (SystemListDialog, Int, String) -> Unit