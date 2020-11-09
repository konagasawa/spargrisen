package com.technology.mycow.spargrisenkt.db

import android.content.Context
import android.telecom.Call
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.technology.mycow.spargrisenkt.AppExecutors
import com.technology.mycow.spargrisenkt.db.dao.TaskDao
//import com.technology.mycow.spargrisenkt.db.dao.TaskWithUserDao
import com.technology.mycow.spargrisenkt.db.dao.UserDao
import com.technology.mycow.spargrisenkt.db.entity.*
//import com.technology.mycow.spargrisenkt.db.dao.UserWithTaskListAndTasksDao

@Database(entities = [User::class, Task::class, TaskAssignUpdate::class, TaskWithPointAndStatusAndUser::class, TaskCompleteUpdate::class], version = 1, exportSchema = false)
abstract class SpargrisenDB : RoomDatabase() {

    private val mIsdatabaseCreated = MutableLiveData<Boolean>()

    abstract fun mUserDao(): UserDao
    abstract fun mTaskDao(): TaskDao
//    abstract fun mTaskWithUserDao(): TaskWithUserDao
//    abstract fun mUserWithTaskListAndTasksDao(): UserWithTaskListAndTasksDao


    private fun updateDatabaseCreated(context: Context){
        if(context.getDatabasePath(DATABASE_NAME).exists()){
            setDatabaseCreated()
        }
    }

    private fun setDatabaseCreated(){
        mIsdatabaseCreated.postValue(true)
    }

    fun getDataBaseCreated() : LiveData<Boolean> {
        return mIsdatabaseCreated
    }

    companion object {

        private const val DB_LOG = "DB_LOG"
        private const val NUMBER_OF_THREAD = 4
        private const val THREAD_SLEEP = 4000
        private var sInstance: SpargrisenDB? = null
        private const val DATABASE_NAME = "SGDB"

        fun getInstance(context: Context, executors: AppExecutors): SpargrisenDB? {
            if(sInstance == null){
                synchronized(SpargrisenDB::class.java){
                    if(sInstance == null){
                        sInstance = createDatabase(context.applicationContext, executors)
                        sInstance!!.updateDatabaseCreated(context.applicationContext)
                    }
                }
            }
            return sInstance
        }

        private fun createDatabase(context: Context, executors: AppExecutors): SpargrisenDB {
            return Room.databaseBuilder(context, SpargrisenDB::class.java, DATABASE_NAME)
                .addCallback(object : Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        executors.localIO.execute {
                            addDelay()
                        }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
        }

        fun addDelay(){
            try {
                Thread.sleep(THREAD_SLEEP.toLong())
            } catch (ie: InterruptedException){

            }
        }
    }


}