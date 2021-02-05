package com.wang.libandroid.filerequest.opera

import android.content.ContentValues
import android.content.Context
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import com.wang.libandroid.filerequest.FileRequest
import com.wang.libandroid.filerequest.FileResponse
import com.wang.libandroid.filerequest.request.Request
import java.io.*
import java.util.*

class AudioRequestImpl : Request {
    override fun createFile(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {
        val suffix = request.displayName?.substring(request.displayName.lastIndexOf("."))
            ?.toLowerCase(Locale.getDefault())
        val values = ContentValues().apply {
            put(MediaStore.Audio.Media.TITLE, "title_1")
            put(MediaStore.Audio.Media.DISPLAY_NAME,request.displayName)
            put(MediaStore.Audio.Media.MIME_TYPE, "audio/${suffix}")
            put(MediaStore.Audio.Media.RELATIVE_PATH, request.relatePath)
        }
        val contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val resolver = context.contentResolver
        val uri = resolver.insert(contentUri, values)
        response(FileResponse(isSuccess = true, uri = uri))
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
        request.uri?.let {
            context.contentResolver.openOutputStream(it)?.use { outPut ->
                var read: Int = -1
                val buffer = ByteArray(2048)
                while ((request.source as InputStream).read(buffer).also { read = it } != -1) {
                    outPut.write(buffer, 0, read)
                }
                outPut.close()
            }
        }
        response(FileResponse(true))
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