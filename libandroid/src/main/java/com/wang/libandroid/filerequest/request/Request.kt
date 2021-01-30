package com.wang.libandroid.filerequest.request

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.wang.libandroid.filerequest.FileRequest
import com.wang.libandroid.filerequest.FileResponse
import java.io.File

/**
 * 定义文件操作的行为
 */
interface Request {

    companion object {
        const val TAG: String = "Divide Store Monitor"
    }

    fun createFile(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    )

    fun deleteFile(
        context: Context, request: FileRequest, response: (fileResponse: FileResponse) -> Unit
    )

    fun updateFile(
        context: Context, request: FileRequest, response: (fileResponse: FileResponse) -> Unit
    )

    fun queryFile(
        context: Context, request: FileRequest, response: (fileResponse: FileResponse) -> Unit
    )

    fun renameTo(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    )

    /**
     * Android 7.0 适配
     */
    fun getFileUri(context: Context, file: File): Uri =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 7.0 及以上，需要授予临时访问权限
            FileProvider.getUriForFile(
                context,
                context.applicationInfo.packageName + ".fileProvider",
                file
            )
        } else {
            Uri.fromFile(file)
        }
}