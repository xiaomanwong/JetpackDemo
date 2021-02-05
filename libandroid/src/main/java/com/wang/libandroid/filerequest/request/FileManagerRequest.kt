package com.wang.libandroid.filerequest.request

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.wang.libandroid.filerequest.FileRequest
import com.wang.libandroid.filerequest.FileResponse
import java.io.*


class FileManagerRequest : Request {

    override fun createFile(
        context: Context,
        request: FileRequest,
        response: (FileResponse) -> Unit
    ) {
        if (request.relatePath.isNullOrEmpty()) {
            throw IllegalArgumentException("路径不完整！")
        }
        val file = newFileWithPath(request.relatePath +"/" +request.displayName)
        response(
            FileResponse(
                file = file,
                uri = getFileUri(context, file!!)
            )
        )
    }

    private fun newFileWithPath(filePath: String): File? {
        if (TextUtils.isEmpty(filePath)) {
            return null
        }
        val index = filePath.lastIndexOf(File.separator)
        var path = ""
        if (index != -1) {
            path = filePath.substring(0, index)
            if (!TextUtils.isEmpty(path)) {
                val file = File(path)
                // 如果文件夹不存在
                if (!file.exists() && !file.isDirectory) {
                    val flag = file.mkdirs()
                    if (flag) {
                        Log.e(
                            Request.TAG,
                            "httpFrame  threadName:" + Thread.currentThread().name + " 创建文件夹成功："
                                    + file.path
                        )
                    } else {
                        Log.e(
                            Request.TAG,
                            "httpFrame  threadName:" + Thread.currentThread().name + " 创建文件夹失败："
                                    + file.path
                        )
                    }
                }
            }
        }
        return File(filePath)
    }

    override fun deleteFile(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {
        if (request.displayName.isNullOrEmpty() || request.relatePath.isNullOrEmpty()) {
            throw IllegalArgumentException("路径不完整！")
        }
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
        val inputStream = if (request.source is InputStream)
            request.source as InputStream
        else throw IllegalArgumentException("参数source为InputStream类型 !")

        try {
            request.file?.apply {
                val out = FileOutputStream(this)
                val buff = ByteArray(1024)
                var len: Int
                while (inputStream!!.read(buff).also { len = it!! } != -1) {
                    out.write(buff, 0, len)
                }
                inputStream!!.close()
                out.close()
            }

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun queryFile(
        context: Context, request: FileRequest, response: (fileResponse: FileResponse) -> Unit
    ) {
        try {
            val file = File(request.relatePath, request.displayName)
            val fis = FileInputStream(file)
            val b = ByteArray(1024)
            var len = 0
            val baos = ByteArrayOutputStream()
            while (fis.read(b).also({ len = it }) != -1) {
                baos.write(b, 0, len)
            }
            val data: ByteArray = baos.toByteArray()
            baos.close()
            fis.close()
            String(data)
            response(FileResponse(isSuccess = true))
        } catch (e: Exception) {
            e.printStackTrace()
            response(FileResponse(isSuccess = false))
        }
    }

    override fun renameTo(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {
        if (request.oldName.isNullOrEmpty() || request.newName.isNullOrEmpty()) {
            throw IllegalArgumentException("重命名文件新名称或旧名称为空！")
        }
        if (!request.oldName.equals(request.newName)) {
            val oldFile = File(request.relatePath + "/" + request.oldName)
            val newFile = File(request.relatePath + "/" + request.newName)
            if (!oldFile.exists()) {
                response(FileResponse(isSuccess = false))
            }
            if (newFile.exists()) {
                response(FileResponse(isSuccess = false))
            } else {
                oldFile.renameTo(newFile)
                response(FileResponse(isSuccess = true))
            }
        }

    }


}