package com.wang.libandroid.filerequest.request

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.ArrayMap
import android.util.Log
import androidx.annotation.RequiresApi
import com.wang.libandroid.filerequest.FileRequest
import com.wang.libandroid.filerequest.FileResponse
import java.io.File

/**
 * Android 10, 11 分区存储
 */
@RequiresApi(Build.VERSION_CODES.Q)
class MediaStoreRequest : Request {

    private val map: ArrayMap<String, Uri> = ArrayMap()

    init {
//        val DIRECTORY_ALARMS = "Alarms"
//        val DIRECTORY_AUDIOBOOKS = "Audiobooks"
//        val DIRECTORY_DCIM = "DCIM"
//        val DIRECTORY_DOCUMENTS = "Documents"
//        val DIRECTORY_DOWNLOADS = "Download"
//        val DIRECTORY_MOVIES = "Movies"
//        val DIRECTORY_MUSIC = "Music"
//        val DIRECTORY_NOTIFICATIONS = "Notifications"
//        val DIRECTORY_PICTURES = "Pictures"
//        val DIRECTORY_PODCASTS = "Podcasts"
//        val DIRECTORY_RINGTONES = "Ringtones"
//        val DIRECTORY_SCREENSHOTS = "Screenshots"
        map[Environment.DIRECTORY_ALARMS] = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        map[Environment.DIRECTORY_AUDIOBOOKS] = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        map[Environment.DIRECTORY_DCIM] = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        map[Environment.DIRECTORY_DOCUMENTS] = MediaStore.Files.getContentUri("external")
        map[Environment.DIRECTORY_DOWNLOADS] = MediaStore.Downloads.EXTERNAL_CONTENT_URI
        map[Environment.DIRECTORY_MOVIES] = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
//        map[Environment.DIRECTORY_NOTIFICATIONS] = MediaStore.
        map[Environment.DIRECTORY_PICTURES] = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//        map[Environment.DIRECTORY_PODCASTS] = MediaStore.
        map[Environment.DIRECTORY_RINGTONES] = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        map[Environment.DIRECTORY_SCREENSHOTS] = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

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
        val value =
            context.contentResolver.delete(
                getFileUri(context, File(request.relatePath!!)),
                null,
                null
            )
        response(FileResponse(value == 1))
    }

    override fun updateFile(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {
        Log.d(Request.TAG, "MediaStoreRequest updateFile: ${request.displayName}")
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