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
import com.wang.libandroid.filerequest.opera.AudioRequestImpl
import com.wang.libandroid.filerequest.opera.FileRequestImpl
import com.wang.libandroid.filerequest.opera.ImageRequestImpl
import com.wang.libandroid.filerequest.opera.MoviesRequestImpl
import java.io.File
import java.util.*

/**
 * Android 10, 11 分区存储
 */
@RequiresApi(Build.VERSION_CODES.Q)
class MediaStoreRequest : Request {

    private val map: ArrayMap<String, Uri> = ArrayMap()

    private val _movieScheme = listOf(
        ".mp4", ".rmvb", ".avi", ".flv", ".3gp", ".wmv", ".mkv", ".mpeg"
    )

    private val _musicScheme = listOf(
        ".mp3", ".wma"
    )

    private val _imageScheme = listOf(
        ".jpg", "jpeg", ".png", ".bmp", ".gif", ".wmf"
    )

    private val _documentScheme = listOf(".txt", ".doc", ".xls")

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
                getFileUri(context, File(request.relatePath)),
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
        val requestImpl = getFileScheme(request)
        requestImpl.updateFile(context, request, response)
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


    private fun getFileScheme(request: FileRequest): Request {
        // 截取后缀
        val suffix =
            request.displayName.substring(request.displayName.lastIndexOf("."))
                .toLowerCase(Locale.getDefault())
        return if (request.relatePath.startsWith(Environment.DIRECTORY_MOVIES)
            && _movieScheme.contains(suffix)
        ) {
            MoviesRequestImpl()
        } else if ((request.relatePath.startsWith(Environment.DIRECTORY_DCIM)
                    || request.relatePath.startsWith(Environment.DIRECTORY_PICTURES)
                    || request.relatePath.startsWith(Environment.DIRECTORY_SCREENSHOTS))
            && _imageScheme.contains(suffix)
        ) {
            ImageRequestImpl()
        } else if ((request.relatePath.startsWith(Environment.DIRECTORY_ALARMS)
                    || request.relatePath.startsWith(Environment.DIRECTORY_MUSIC)
                    || request.relatePath.startsWith(Environment.DIRECTORY_RINGTONES))
            && _musicScheme.contains(suffix)
        ) {
            AudioRequestImpl()
        } else if (request.relatePath.startsWith(Environment.DIRECTORY_DOCUMENTS)) {
            FileRequestImpl()
        } else if (request.relatePath.startsWith(Environment.DIRECTORY_DOWNLOADS)) {
            FileRequestImpl()
        } else {
            FileRequestImpl()
        }
    }
}