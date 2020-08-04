package com.example.myapplication.mode

import android.text.TextUtils
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import java.io.Serializable

class Feed : BaseObservable(), Serializable {

    val TYPE_IMAGE_TEXT = 1 //图文

    val TYPE_VIDEO = 2 //视频

    /**
     * id : 364
     * itemId : 6739143063064549000
     * itemType : 2
     * createTime : 1569079017
     * duration : 299.435
     * feeds_text : 当中国地图出来那一幕，我眼泪都出来了！
     * 太震撼了！
     * authorId : 3223400206308231
     * activityIcon : null
     * activityText : null
     * width : 640
     * height : 368
     * url : https://pipijoke.oss-cn-hangzhou.aliyuncs.com/6739143063064549643.mp4
     * cover :
     */
    val id = 0
    private val itemId: Long = 0
    private val itemType = 0
    private val createTime: Long = 0
    private val duration = 0.0
    private val feeds_text: String? = null
    private val authorId: Long = 0
    private val activityIcon: String? = null
    private val activityText: String? = null
    private val width = 0
    private val height = 0
    private val url: String? = null
    private val cover: String? = null

    private val author: User? = null
        get() = field
    private val topComment: Comment? = null
    private var ugc: Ugc? = null
        @Bindable
        get() {
            if (field == null) {
                field = Ugc()
            }
            return field
        }


    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Feed) return false
        val newFeed: Feed = other
        return (id == newFeed.id && itemId == newFeed.itemId && itemType == newFeed.itemType && createTime == newFeed.createTime && duration == newFeed.duration && TextUtils.equals(
            feeds_text,
            newFeed.feeds_text
        )
                && authorId == newFeed.authorId && TextUtils.equals(
            activityIcon,
            newFeed.activityIcon
        )
                && TextUtils.equals(activityText, newFeed.activityText)
                && width == newFeed.width && height == newFeed.height && TextUtils.equals(
            url,
            newFeed.url
        )
                && TextUtils.equals(cover, newFeed.cover)
                && author != null && author!! == newFeed.author
                && topComment != null && topComment == newFeed.topComment
                && ugc != null && ugc!! == newFeed.ugc)
    }
}

