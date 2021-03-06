package com.eatradish.videoweightloss.utils

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object ThreadPoolUtil {
    fun executeSingleThreadPool(runnable: Runnable): ExecutorService {
        val executor = Executors.newSingleThreadExecutor()
        executor.submit(runnable)
        return executor
    }
}
