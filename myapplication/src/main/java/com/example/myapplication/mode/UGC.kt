package com.example.myapplication.mode

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import java.io.Serializable

class Ugc : BaseObservable(), Serializable {
    /**
     * likeCount : 153
     * shareCount : 0
     * commentCount : 4454
     * hasFavorite : false
     * hasLiked : true
     * hasdiss:false
     */
    var likeCount = 0

    var shareCount = 0
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(com.example.myapplication.BR._all)
        }

    var commentCount = 0
    var hasFavorite: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(com.example.myapplication.BR._all)
        }
        @Bindable
        get() = field

    var hasdiss = false
        @Bindable
        get() = field
        set(value) {
            if (field == value) {
                return
            }
            if (hasdiss) {
                hasLiked = false
            }
            field = hasdiss
            notifyPropertyChanged(com.example.myapplication.BR._all)
        }


    var hasLiked = false
        @Bindable
        get() = field
        set(value) {
            if (field == value) return
            if (field) {
                likeCount += 1
                hasdiss = false
            } else {
                likeCount -= 1
            }
            field = hasLiked
            notifyPropertyChanged(com.example.myapplication.BR._all)
        }

    override fun equals(obj: Any?): Boolean {
        if (obj == null || obj !is Ugc) return false
        return likeCount == obj.likeCount && shareCount == obj.shareCount && commentCount == obj.commentCount
                && hasFavorite == obj.hasFavorite && hasLiked == obj.hasLiked && hasdiss == obj.hasdiss
    }

    override fun hashCode(): Int {
        var result = likeCount
        result = 31 * result + commentCount
        return result
    }

}