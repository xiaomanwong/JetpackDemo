package com.example.myapplication.media.player2


import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloadQueueSet
import com.liulishuo.filedownloader.FileDownloader

typealias DownloadSuccess_CALLBACK_T = ((String, String) -> Unit)

/**
 * @author aosyang
 * @date 2022/5/20
 * @Desc
 */
class TinyDownloader {
    private val downloadListener = createLis()
    private val queueSet = FileDownloadQueueSet(downloadListener)
    val tasks = ArrayList<BaseDownloadTask>()
    var successCall: DownloadSuccess_CALLBACK_T? = null

    fun addTask(remote: String, local: String) {
        tasks.add(
            FileDownloader.getImpl().create(remote).setPath(local).setTag(Pair(remote,local))
        )
    }

    fun start(whenSuccess: DownloadSuccess_CALLBACK_T?) {
        successCall = whenSuccess
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
                val p = task.tag as Pair<String, String>
                successCall?.invoke(p.first, p.second)
            }

            override fun paused(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {

            }

            override fun error(task: BaseDownloadTask, e: Throwable) {
            }

            override fun warn(task: BaseDownloadTask) {

            }
        }
    }
}