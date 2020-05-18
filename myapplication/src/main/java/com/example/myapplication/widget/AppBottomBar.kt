package com.example.myapplication.widget;

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MenuItem
import com.example.myapplication.R
import com.example.myapplication.mode.BottomBar
import com.example.myapplication.util.getBottomConfig
import com.example.myapplication.util.getDestConfig
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode

class AppBottomBar @SuppressLint("RestrictedApi") constructor(
    context: Context?,
    attributes: AttributeSet?,
    defStyleAttributes: Int
) : BottomNavigationView(context!!, attributes, defStyleAttributes) {

    @JvmOverloads
    constructor(context: Context?, attrs: AttributeSet? = null) : this(
        context,
        attrs,
        0
    )

    var mBottomBar: BottomBar? = null

    /**
     * 所有底部按钮的 icon
     */
    companion object {
        val icons = intArrayOf(
            R.mipmap.icon_tab_home,
            R.mipmap.icon_tab_sofa,
            R.mipmap.icon_tab_publish,
            R.mipmap.icon_tab_find,
            R.mipmap.icon_tab_mine
        )
    }

    init {
        mBottomBar = getBottomConfig()
        println("tabs: $mBottomBar")
        // 定义选中状态
        val state: Array<IntArray?> = arrayOfNulls(2)
        state[0] = intArrayOf(android.R.attr.state_selected)
        state[1] = intArrayOf()
        val colorStateList = ColorStateList(
            state, intArrayOf(
                Color.parseColor(mBottomBar?.activeColor),
                Color.parseColor(mBottomBar?.inActiveColor)
            )
        )
        itemTextColor = colorStateList
        itemIconTintList = colorStateList

        /*
          自动判断
          int LABEL_VISIBILITY_AUTO = -1;

          选中时，显示文本
          int LABEL_VISIBILITY_SELECTED = 0;

          无论什么状态都显示文本
          int LABEL_VISIBILITY_LABELED = 1;

          无论什么状态都不显示文本
          int LABEL_VISIBILITY_UNLABELED = 2;*/
        labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        // 设置选中 item
        selectedItemId = mBottomBar?.selectTab!!

        val tabs = mBottomBar?.tabs
        for (tab in tabs!!) {
            if (!tab.enable) {
                continue
            }
            val itemId = getItemId(tab.pageUrl)

            if (itemId < 0) {
                continue
            }

            val menuItem: MenuItem = menu.add(0, itemId, tab.index, tab.title)
            menuItem.setIcon(icons[tab.index])
        }

        var index = 0
        for (tab in mBottomBar?.tabs!!) {
            if (!tab.enable) {
                continue
            }

            val itemId = getItemId(tab.pageUrl)
            if (itemId < 0) continue

            val iconSize = dp2px(tab.size)
            val menuView: BottomNavigationMenuView = getChildAt(0) as BottomNavigationMenuView
            val menuItem = menuView.getChildAt(index) as BottomNavigationItemView
            menuItem.setIconSize(iconSize)

            if (tab.title?.isEmpty()!!) {
                val tineColor =
                    if (TextUtils.isEmpty(tab.tintColor)) Color.parseColor("#ff678f") else Color.parseColor(
                        tab.tintColor
                    )
                menuItem.setIconTintList(ColorStateList.valueOf(tineColor))
                // 禁止点按时，上下浮动效果
                menuItem.setShifting(false)
            }
            index++
        }
    }

    private fun getItemId(pageUrl: String?): Int {
        val destinationMap = getDestConfig();
        if (!destinationMap.containsKey(pageUrl)) {
            return -1
        }
        val destination = destinationMap[pageUrl]
        return destination?.id!!
    }

    fun dp2px(size: Int): Int {
        return (context.resources.displayMetrics.density * size + 0.5f).toInt()
    }

}