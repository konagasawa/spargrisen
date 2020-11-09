package com.technology.mycow.spargrisenkt

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors private constructor (
    val localIO: Executor,
    val networkIO: Executor,
    val mainThread: Executor
) {

    constructor() : this (
        Executors.newSingleThreadExecutor(),
        Executors.newFixedThreadPool(NUMBER_OF_EXECUTOR_THREAD),
        MainThreadExecutor()
    ) {}

    companion object {
        private const val NUMBER_OF_EXECUTOR_THREAD = 3

        private class MainThreadExecutor : Executor {
            private val mainThreadHandler =
                Handler(Looper.getMainLooper())

            override fun execute(command: Runnable) {
                mainThreadHandler.post(command)
            }
        }
    }

}