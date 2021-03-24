package com.wang.libandroid.filerequest.opera

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import androidx.annotation.RequiresApi
import com.wang.libandroid.filerequest.FileRequest
import com.wang.libandroid.filerequest.FileResponse
import com.wang.libandroid.filerequest.request.Request
import java.io.InputStream
import java.util.*


class FileRequestImpl : Request {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun createFile(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {
        val suffix =
            request.displayName?.substring(request.displayName.lastIndexOf("."))
                ?.toLowerCase(Locale.getDefault())
        val values = if (TextUtils.equals(request.relatePath, Environment.DIRECTORY_DOWNLOADS)) {
            buildDownloadContentValues(request.relatePath, suffix)
        } else {
            buildDocumentsContentValues(request.relatePath, suffix)
        }

        val contentUri =
            if (TextUtils.equals(request.relatePath, Environment.DIRECTORY_DOWNLOADS)) {
                MediaStore.Downloads.EXTERNAL_CONTENT_URI
            } else {
                MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
            }
        val uri = context.contentResolver.insert(contentUri, values)
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
        context.contentResolver.openOutputStream(request.uri!!)?.use { outPut ->
            var read: Int = -1
            val buffer = ByteArray(2048)
            while ((request.source as InputStream).read(buffer)
                    .also { read = it } != -1
            ) {
                outPut.write(buffer, 0, read)
            }
            outPut.close()
        }
        response(FileResponse(true))

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun queryFile(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {

        var pathKey = MediaStore.Files.FileColumns.RELATIVE_PATH
        var pathValue = request.relatePath
        val contentUri =
            if (TextUtils.equals(request.relatePath, Environment.DIRECTORY_DOWNLOADS)) {
                MediaStore.Downloads.EXTERNAL_CONTENT_URI
            } else {
                MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
            }
        val cursor = context.contentResolver.query(
            contentUri,
            null,
            if (pathKey.isEmpty()) {
                null
            } else {
                "$pathKey LIKE ?"
            },
            if (pathValue.isEmpty()) {
                null
            } else {
                arrayOf("%$pathValue%")
            },
            null
        )

        cursor?.also {
            while (it.moveToNext()) {
                val id = it.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID))
                val displayName =
                    it.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                val uri =
                    ContentUris.withAppendedId(contentUri, id)
                if (TextUtils.equals(displayName, request.displayName)) {
                    response(FileResponse(isSuccess = true, uri = uri))
                }
            }
        }
        cursor?.close()


    }

    override fun renameTo(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {

    }

    private fun buildDownloadContentValues(dirName: String, suffix: String): ContentValues {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "title_1")
        values.put(
            MediaStore.Downloads.DISPLAY_NAME,
            "file_${System.currentTimeMillis()}$suffix"
        )
        values.put(
            MediaStore.Downloads.RELATIVE_PATH,
            "$dirName/down"
        )
        return values
    }

    private fun buildDocumentsContentValues(dirName: String, suffix: String): ContentValues {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "title_1")
        values.put(
            MediaStore.Downloads.DISPLAY_NAME,
            "file_${System.currentTimeMillis()}$suffix"
        )
        values.put(
            MediaStore.Downloads.RELATIVE_PATH,
            "$dirName/doc"
        )
        return values
    }
}