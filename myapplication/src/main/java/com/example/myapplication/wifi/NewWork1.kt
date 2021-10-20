package com.example.myapplication.wifi

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.lang.Exception

class NewWork1(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    /**
     *  后台任务，且异步执行
     */
    override fun doWork(): Result {
        Log.d(TAG, "doWork: start")
        try {

            Thread.sleep(10000)
        } catch (e: Exception) {
            Log.d(TAG, "doWork: error")
            return Result.failure()
        }
        Log.d(TAG, "doWork: finish")
        return Result.success()
    }

    companion object {
        private const val TAG = "UploadWork"
    }

}