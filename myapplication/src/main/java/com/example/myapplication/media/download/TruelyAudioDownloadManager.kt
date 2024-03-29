package com.yidian.local.service.audio.download

import android.util.Log
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloadQueueSet
import com.liulishuo.filedownloader.FileDownloader
import com.yidian.local.service.audio.model.TruelyAudioElement

/**
 * @author admin
 * @date 2022/5/6
 * @Desc
 */
object TruelyAudioDownloadManager {

    @Synchronized
    fun startDownload(element: ArrayList<TruelyAudioElement>, downloadAndPlay: Boolean = false) {
        val downloadListener = createLis()
        val queueSet = FileDownloadQueueSet(downloadListener)
        val tasks: MutableList<BaseDownloadTask> = ArrayList()

        element.forEachIndexed { index, it ->
            if (index == 0 && downloadAndPlay) {
                it.isFirst = true
            }
            it.startTime = System.currentTimeMillis()
            tasks.add(
                FileDownloader.getImpl()
                    .create(it.path)
                    .setPath(it.localPath)
                    .setTag(it)
            )
        }
        // do not want each task's download progress's callback,
        queueSet.disableCallbackProgressTimes()
        // auto retry 1 time if download fail
        queueSet.setAutoRetryTimes(1)
        // 设置队列一起下载
        queueSet.downloadSequentially(tasks)
        // we just consider which task will completed.
        queueSet.start()
    }


    private fun createLis(): FileDownloadListener {
        return object : FileDownloadListener() {
            override fun pending(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
                // 之所以加这句判断，是因为有些异步任务在pause以后，会持续回调pause回来，而有些任务在pause之前已经完成，
                // 但是通知消息还在线程池中还未回调回来，这里可以优化
                // 后面所有在回调中加这句都是这个原因
            }

            override fun connected(
                task: BaseDownloadTask, etag: String, isContinue: Boolean,
                soFarBytes: Int, totalBytes: Int
            ) {
                super.connected(task, etag, isContinue, soFarBytes, totalBytes)
            }

            override fun progress(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
            }

            override fun blockComplete(task: BaseDownloadTask) {
            }

            override fun retry(
                task: BaseDownloadTask,
                ex: Throwable,
                retryingTimes: Int,
                soFarBytes: Int
            ) {
                super.retry(task, ex, retryingTimes, soFarBytes)
            }

            override fun completed(task: BaseDownloadTask) {
                val element: TruelyAudioElement = task.tag as TruelyAudioElement
                element.endTime = System.currentTimeMillis()
                if (element.isFirst) {
                    element.player.downloadCompleteAndPlay(element)
                }
            }

            override fun paused(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
            }

            override fun error(task: BaseDownloadTask, e: Throwable) {
                val element: TruelyAudioElement = task.tag as TruelyAudioElement
                element.endTime = -1
                if (element.isFirst) {
                    element.player.downloadCompleteAndPlay(element)
                }
            }

            override fun warn(task: BaseDownloadTask) {
            }
        }
    }
}