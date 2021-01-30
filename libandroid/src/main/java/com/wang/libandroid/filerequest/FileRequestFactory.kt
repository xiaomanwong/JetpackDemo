package com.wang.libandroid.filerequest

import android.os.Build
import android.os.Environment
import com.wang.libandroid.filerequest.request.FileManagerRequest
import com.wang.libandroid.filerequest.request.MediaStoreRequest
import com.wang.libandroid.filerequest.request.Request

object FileRequestFactory {

    fun getRequest(): Request =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // 已经是 10，但使用的是兼容模式
            if (!Environment.isExternalStorageLegacy()) {
                FileManagerRequest()
            } else {
                MediaStoreRequest()
            }
        } else {
            FileManagerRequest()
        }
}