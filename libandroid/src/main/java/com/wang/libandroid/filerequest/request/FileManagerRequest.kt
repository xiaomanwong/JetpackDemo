package com.wang.libandroid.filerequest.request

import android.content.Context
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
        val file = File(request.relatePath, request.displayName)
        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()
    }


    override fun deleteFile(
        context: Context,
        request: FileRequest,
        response: (fileResponse: FileResponse) -> Unit
    ) {
        if (request.displayName.isNullOrEmpty() || request.relatePath.isNullOrEmpty()) {
            throw IllegalArgumentException("路径不完整！")
        }
        val file = File(request.relatePath, request.displayName)
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
                val outStream = FileOutputStream(this)
                outStream.write(input2byte(inputStream))
                outStream.close()
            }

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    fun input2byte(inStream: InputStream): ByteArray {
        val swapStream = ByteArrayOutputStream()
        val buff = ByteArray(100)
        var rc = 0
        while (inStream.read(buff, 0, 100).also { rc = it } > 0) {
            swapStream.write(buff, 0, rc)
        }
        return swapStream.toByteArray()
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