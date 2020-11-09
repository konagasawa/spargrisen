package com.technology.mycow.spargrisenkt

import android.app.Application
import android.util.Log
import com.technology.mycow.spargrisenkt.db.SpargrisenDB

class Spargrisen : Application() {

    lateinit private var mAppExecutors: AppExecutors

    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_MSG, "THIS IS BEGINNING")
        mAppExecutors = AppExecutors()
    }

    private fun getDatabase(): SpargrisenDB? {
        return SpargrisenDB.getInstance(this, mAppExecutors)
    }

    fun getDataRepository(): DataRepository? {
        return getDatabase()?.let { DataRepository.getInstance(it, mAppExecutors) }
    }

//    val database: SpargrisenDB
//        get() = SpargrisenDB.getInstance(this, mAppExecutors)
//
//    val repository: DataRepository
//        get() = DataRepository.getInstance(database, mAppExecutors)

    companion object {
        private const val LOG_MSG = "SPARGRISEN APP: "
    }
}