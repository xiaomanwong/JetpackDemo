package com.wang.libandroid.filerequest

import android.net.Uri
import java.io.File

/**
 * 操作结果集合
 */
class FileResponse() {
    var isSuccess: Boolean = false
    var file: File? = null
    var uri: Uri? = null
    var path: String? = null


    constructor(
        isSuccess: Boolean = true,
        file: File? = null,
        uri: Uri? = null,
        path: String? = null
    ) : this() {
        this.isSuccess = isSuccess
        this.file = file
        this.uri = uri
        this.path = path
    }
}