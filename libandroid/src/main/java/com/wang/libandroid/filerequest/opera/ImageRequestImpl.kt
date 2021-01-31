package com.wang.libandroid.filerequest.opera

import android.content.Context
import com.wang.libandroid.filerequest.FileRequest
import com.wang.libandroid.filerequest.FileResponse
import com.wang.libandroid.filerequest.request.Request

class ImageRequestImpl : Request {
    override fun createFile(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {

    }

    override fun deleteFile(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {

    }

    override fun updateFile(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {

    }

    override fun queryFile(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {

    }

    override fun renameTo(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {

    }
}