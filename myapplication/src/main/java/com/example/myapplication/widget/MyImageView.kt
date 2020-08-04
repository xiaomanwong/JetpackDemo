package com.example.myapplication.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop

class MyImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {


    companion object {
        @BindingAdapter(value = ["image_url", "isCircle"], requireAll = false)
        fun setImageUrl(view: MyImageView, url: String, isCircle: Boolean) {
            val builder = Glide.with(view).load(url)
            if (isCircle) {
                builder.transform(CircleCrop())
            }

            val layoutParams = view.layoutParams
            if (view.layoutParams != null && layoutParams.width > 0 && layoutParams.height > 0) {
                builder.override(layoutParams.width, layoutParams.height)
            }

            builder.into(view)
        }
    }
}