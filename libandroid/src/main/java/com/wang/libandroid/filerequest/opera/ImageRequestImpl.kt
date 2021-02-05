package com.wang.libandroid.filerequest.opera

import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.IntentSender
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import com.wang.libandroid.filerequest.FileRequest
import com.wang.libandroid.filerequest.FileResponse
import com.wang.libandroid.filerequest.request.Request
import java.io.File
import java.util.*

class ImageRequestImpl : Request {
    override fun createFile(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {
        val suffix = request.displayName?.substring(request.displayName.lastIndexOf("."))
            ?.toLowerCase(Locale.getDefault())
        val mimeType = "image/${suffix}"
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, request.displayName)
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, request.relatePath)
        val uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
        )
        response(FileResponse(isSuccess = true, uri = uri))
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun deleteFile(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {
        var row = 0
        try {
            // Android 10+中,如果删除的是其它应用的Uri,则需要用户授权
            // 会抛出RecoverableSecurityException异常
            var imageUri: Uri? = null
            queryFile(context,FileRequest(request.relatePath, request.displayName)){
                if (it.uri == null){
                    response(FileResponse(isSuccess = false))
                }else{
                    imageUri = it.uri
                }
            }
            row = context.contentResolver.delete(imageUri!!, null, null)
        } catch (securityException: SecurityException) {
            securityException as? RecoverableSecurityException
                ?: throw securityException
        }

        if (row > 0) {
            response(FileResponse(isSuccess = true))
        }
    }



    override fun updateFile(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {
        request.uri?.apply {
            val bitmap = request.source as Bitmap
            val compressFormat = Bitmap.CompressFormat.JPEG
            val outputStream = context.contentResolver.openOutputStream(request.uri!!)
            outputStream?.also { os ->
                bitmap.compress(compressFormat, 100, os)
                os.close()
                response(FileResponse(true))
            }
        }
    }

    override fun queryFile(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {

        var pathKey = MediaStore.MediaColumns.RELATIVE_PATH
        var pathValue = request.relatePath

        try {
            val dataList = mutableListOf<ImageBean>()
            val cursor = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
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
                "${MediaStore.MediaColumns.DATE_ADDED} desc"
            )

            cursor?.also {
                while (it.moveToNext()) {
                    val id = it.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                    val displayName =
                        it.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
                    val uri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    if (TextUtils.equals(displayName, request.displayName)){
                        response(FileResponse(isSuccess = true, uri = uri))
                    }
                    dataList.add(ImageBean(id, uri, displayName))
                }
            }
            cursor?.close()
        }catch (e: Exception){
            response(FileResponse(isSuccess = false))
        }


    }

    override fun renameTo(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {

    }

    data class ImageBean(val id: Long, val uri: Uri, val displayName: String)
}