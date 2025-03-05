package com.example.myapplication.ui

import android.app.ActivityOptions
import android.os.Bundle

/**
 * @author Quinn
 * @date 2023/8/31
 * @Desc
 */
data class ItemModel(
    val itemName: String,
    val itemPath: String,
//    val bundle: Bundle? = null,
    val options: Bundle? = null,
)
