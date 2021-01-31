package com.wang.libandroid.filerequest.opera

import android.content.Context
import com.wang.libandroid.filerequest.FileRequest
import com.wang.libandroid.filerequest.FileResponse
import com.wang.libandroid.filerequest.request.Request

class FileRequestImpl:Request {
    override fun createFile(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun deleteFile(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun updateFile(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun queryFile(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun renameTo(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {
        TODO("Not yet implemented")
    }
}