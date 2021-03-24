package com.wang.libandroid.filerequest.opera

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.net.toFile
import com.wang.libandroid.filerequest.FileRequest
import com.wang.libandroid.filerequest.FileResponse
import com.wang.libandroid.filerequest.request.Request
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.FileInputStream
import java.io.IOException
import java.util.*

class MoviesRequestImpl : Request {
    override fun createFile(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {
        val suffix = request.displayName?.substring(request.displayName.lastIndexOf("."))
            ?.toLowerCase(Locale.getDefault())
        val values = ContentValues()
        val name: String = request.displayName
        values.put(MediaStore.Video.Media.DISPLAY_NAME, name)
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/${suffix}")
        values.put(MediaStore.Video.Media.IS_PENDING, 1)
        val resolver = context.contentResolver
        val collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val uri = resolver.insert(collection, values)
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
        try {
            context.contentResolver.openFileDescriptor(request.uri!!, "w", null).use { pfd ->
                val bin = BufferedInputStream(FileInputStream(request.displayName))
                val outputStream = ParcelFileDescriptor.AutoCloseOutputStream(pfd)
                val bot = BufferedOutputStream(outputStream)
                val bt = ByteArray(2048)
                var len: Int
                while (bin.read(bt).also { len = it } >= 0) {
                    bot.write(bt, 0, len)
                    bot.flush()
                }
                bin.close()
                bot.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        response(FileResponse(true))

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