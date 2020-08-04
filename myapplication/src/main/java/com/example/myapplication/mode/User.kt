package com.example.myapplication.mode

import android.text.TextUtils
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import java.io.Serializable

class User : BaseObservable(), Serializable {
    /**
     * id : 962
     * userId : 3223400206308231
     * name : 二师弟请随我来
     * avatar :
     * description :
     * likeCount : 0
     * topCommentCount : 0
     * followCount : 0
     * followerCount : 0
     * qqOpenId : null
     * expires_time : 0
     * score : 0
     * historyCount : 0
     * commentCount : 0
     * favoriteCount : 0
     * feedCount : 0
     * hasFollow : false
     */
    var id = 0
    var userId: Long = 0
    var name: String? = null
    var avatar: String? = null
    var description: String? = null
    var likeCount = 0
    var topCommentCount = 0
    var followCount = 0
    var followerCount = 0
    var qqOpenId: String? = null
    var expires_time: Long = 0
    var score = 0
    var historyCount = 0
    var commentCount = 0
    var favoriteCount = 0
    var feedCount = 0
    var hasFollow: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(com.example.myapplication.BR._all)
        }

    override fun equals(obj: Any?): Boolean {
        if (obj == null || obj !is User) return false
        val newUser = obj
        return (TextUtils.equals(name, newUser.name)
                && TextUtils.equals(avatar, newUser.avatar)
                && TextUtils.equals(description, newUser.description)
                && likeCount == newUser.likeCount && topCommentCount == newUser.topCommentCount && followCount == newUser.followCount && followerCount == newUser.followerCount && qqOpenId === newUser.qqOpenId && expires_time == newUser.expires_time && score == newUser.score && historyCount == newUser.historyCount && commentCount == newUser.commentCount && favoriteCount == newUser.favoriteCount && feedCount == newUser.feedCount && hasFollow == newUser.hasFollow)
    }

    @Bindable
    fun isHasFollow(): Boolean {
        return hasFollow
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + userId.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (avatar?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + likeCount
        result = 31 * result + topCommentCount
        result = 31 * result + followCount
        result = 31 * result + followerCount
        result = 31 * result + (qqOpenId?.hashCode() ?: 0)
        result = 31 * result + expires_time.hashCode()
        result = 31 * result + score
        result = 31 * result + historyCount
        result = 31 * result + commentCount
        result = 31 * result + favoriteCount
        result = 31 * result + feedCount
        return result
    }
}