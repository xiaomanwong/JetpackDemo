package com.wang.libandroid.filerequest.request

import android.content.Context
import android.util.Log
import com.wang.libandroid.filerequest.FileRequest
import com.wang.libandroid.filerequest.FileResponse
import java.io.File

class FileManagerRequest : Request {

    override fun createFile(
        context: Context,
        request: FileRequest,
        response: (FileResponse) -> Unit
    ) {
        val newFile = File(request.relatePath)
        if (!newFile.exists()) {
            newFile.mkdirs()
        }
        response(
            FileResponse(
                file = newFile,
                uri = getFileUri(context, newFile)
            )
        )
    }

    override fun deleteFile(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {
        val file = File(request.relatePath)
        var result = false
        if (file.exists()) {
            result = file.delete()
        }
        response(FileResponse(result, file))
    }

    override fun updateFile(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {
        Log.d(Request.TAG, "FileManagerRequest updateFile: ${request.displayName}")
    }

    override fun queryFile(
        context: Context, request: FileRequest, response: (fileResponse: FileResponse) -> Unit
    ) {

    }

    override fun renameTo(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {

    }


}