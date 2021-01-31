package com.wang.libandroid.filerequest.opera

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.net.toFile
import com.wang.libandroid.filerequest.FileRequest
import com.wang.libandroid.filerequest.FileResponse
import com.wang.libandroid.filerequest.request.Request

class MoviesRequestImpl : Request {
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
        response(FileResponse())
        Log.d(Request.TAG, "MoviesRequestImpl updateFile: ")
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun queryFile(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {
        val videoCursor: Cursor? = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.RELATIVE_PATH,
                MediaStore.Video.Media.MIME_TYPE
            ),
            null, null, null
        )

        val uri: Uri = ContentUris.withAppendedId(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            videoCursor?.getLong(videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID))!!
        )
        videoCursor.close()
        response(
            FileResponse(true, uri.toFile(), uri, uri.toFile().absolutePath)
        )
    }

    override fun renameTo(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {

    }
}