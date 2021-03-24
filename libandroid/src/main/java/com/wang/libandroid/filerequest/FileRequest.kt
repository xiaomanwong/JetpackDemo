package com.wang.libandroid.filerequest

import android.net.Uri
import java.io.File


class FileRequest() {
    var relatePath: String = ""
    var displayName: String = ""
    var uri: Uri? = null
    var file: File? = null
    var source: Any? = null
    var oldName: String? = null
    var newName: String? = null


    constructor(
        relatePath: String = "",
        displayName: String = "",
        uri: Uri? = null,
        file: File? = null,
        source: Any? = null,
        oldName: String? = null,
        newName : String ? = null
    ) : this() {
        this.relatePath = relatePath
        this.displayName = displayName
        this.uri = uri
        this.file = file
        this.source = source
        this.oldName = oldName
        this.newName = newName
    }
}