package com.example.myapplication.widget

import android.content.Context
import android.util.AttributeSet
import xyz.doikki.videoplayer.player.VideoView

/**
 * author : ldw
 * date : 2023/4/23
 */
class CacheVideoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : VideoView(context, attrs, defStyleAttr) {
    var useCache = true

    fun getUrl(): String = mUrl

    override fun setUrl(url: String?) {
        if (!useCache || url.isNullOrEmpty()) return super.setUrl(url)
        if (!url.startsWith("http") || !url.startsWith("https")) return super.setUrl(url)
//        super.setUrl(cacheManager.getVideoPath(url))
    }
}