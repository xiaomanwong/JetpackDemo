package com.example.myapplication.custom_page

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.R
import com.google.android.material.imageview.ShapeableImageView
import kotlin.math.min

/**
 * @author Quinn
 * @date 2024/5/17
 * @Desc
 */
class IdProfileHeaderView(context: Context, attr: AttributeSet) : ConstraintLayout(context, attr) {


    private val userImage: ShapeableImageView
    private val idImage: AppCompatImageView

    init {
        inflate(context, R.layout.layout_profile_user_single, this)
        userImage = findViewById(R.id.userImage)
        idImage = findViewById(R.id.idImage)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)

        // Set the size of userImage
        val userImageSize = min(parentWidth, parentHeight)
        val userImageLayoutParams = userImage.layoutParams
        val gap = if (userImageSize > 42 * 3) 18 else 12
        userImageLayoutParams.width = userImageSize
        userImageLayoutParams.height = userImageSize
        userImage.layoutParams = userImageLayoutParams

        // Set the size of idImage to be 0.26 times the size of userImage
        val idImageSize = (userImageSize / 2.6).toInt()
        val idImageLayoutParams = idImage.layoutParams
        idImageLayoutParams.width = idImageSize
        idImageLayoutParams.height = idImageSize
        idImage.layoutParams = idImageLayoutParams
        setMeasuredDimension(parentWidth + gap, userImageSize)
    }

//    fun updateImages(userImageSrc: String, idImageSrc: String) {
        // 更新 userImage 的 src   x/y = 2.6/1
//        GlideModule.with(binding.userImage)
//            .load(userImageSrc)
//            .placeholder(R.drawable.icon_group_user_avatar_place)
//            .error(R.drawable.icon_group_user_avatar_place)
//            .into(binding.userImage)
//
//        // 更新 idImage 的 src
//        GlideModule.with(binding.idImage)
//            .load(idImageSrc)
//            .placeholder(R.drawable.icon_group_user_avatar_place)
//            .error(R.drawable.icon_group_user_avatar_place)
//            .into(binding.idImage)
//    }
}