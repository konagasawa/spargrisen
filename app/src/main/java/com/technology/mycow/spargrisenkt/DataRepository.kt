package com.technology.mycow.spargrisenkt

import android.util.Log
import androidx.lifecycle.LiveData
import com.technology.mycow.spargrisenkt.db.SpargrisenDB
import com.technology.mycow.spargrisenkt.db.dao.TaskDao
//import com.technology.mycow.spargrisenkt.db.dao.TaskWithUserDao
import com.technology.mycow.spargrisenkt.db.dao.UserDao
import com.technology.mycow.spargrisenkt.db.entity.*
//import com.technology.mycow.spargrisenkt.db.dao.UserWithTaskListAndTasksDao
//import com.technology.mycow.spargrisenkt.db.entity.TaskList
//import com.technology.mycow.spargrisenkt.db.entity.TaskWithUser

class DataRepository private constructor(private val mSpargrisenDB: SpargrisenDB,
                                         private val mExecutors: AppExecutors){

    private val mUserDao: UserDao
    private val mTaskDao: TaskDao
//    private val mUserWithTaskListAndTasksDao: UserWithTaskListAndTasksDao
//    private val mTaskWithUserkDao: TaskWithUserDao
    private val mUser: LiveData<List<User>>
    private val mTask: LiveData<List<Task>>

    private var mUserByUserId: LiveData<User>

    init {
        mUserDao = mSpargrisenDB.mUserDao()
        mTaskDao = mSpargrisenDB.mTaskDao()
//        mUserWithTaskListAndTasksDao = mSpargrisenDB.mUserWithTaskListAndTasksDao()
//        mTaskWithUserkDao = mSpargrisenDB.mTaskWithUserDao()
        mUser = mSpargrisenDB.mUserDao().getAllUser()
        mTask = mSpargrisenDB.mTaskDao().getAllTask()

        mUserByUserId = mSpargrisenDB.mUserDao().getUserByUserId(100)

        Log.d(LOG_MSG, "REPO INITIALIZED")
    }

    val user: LiveData<List<User>>
        get() = mSpargrisenDB.mUserDao().getAllUser()

    fun setUserByUserId(userId: Long){
        mUserByUserId = mSpargrisenDB.mUserDao().getUserByUserId(userId)
    }

    val userByUserId: LiveData<User>
        get() = mUserByUserId

    fun getTaskByUser(userId: Long): LiveData<List<Task>> {
        return mSpargrisenDB.mTaskDao().getTaskByUser(userId)
    }

    fun getTaskByUserSortedByStatus(userId: Long, taskStatus: String): LiveData<List<Task>> {
        return mSpargrisenDB.mTaskDao().getTaskByUserSortedByStatus(userId, taskStatus)
    }

    fun taskWithSortedByStatus(taskStatus: String): LiveData<List<Task>> {
        return mSpargrisenDB.mTaskDao().getTaskWithSortedByStatus(taskStatus)
    }

    fun insertUser(user: User){
        return mSpargrisenDB.mUserDao().insertUser(user)
    }

    fun deleteUser(userId: Long){
        return mSpargrisenDB.mUserDao().deleteUser(userId)
    }

    val task: LiveData<List<Task>>
        get() = mSpargrisenDB.mTaskDao().getAllTask()

    fun insertTask(task: Task){
        return mSpargrisenDB.mTaskDao().insertTask(task)
    }

    fun updateTaskToAssign(taskAssignUpdate: TaskAssignUpdate){
        return mSpargrisenDB.mTaskDao().updateTaskToAssign(taskAssignUpdate)
    }

    fun updateTaskToComplete(taskCompleteUpdate: TaskCompleteUpdate){
        return mSpargrisenDB.mTaskDao().updateTaskToComplete(taskCompleteUpdate)
    }

    fun getPointOfTask(userId: Long, startDate: Long, endDate: Long): List<TaskWithPointAndStatusAndUser> {
        return mSpargrisenDB.mTaskDao().getPointOfTask(userId, startDate, endDate)
    }

    fun deleteTask(taskId: Long){
        mSpargrisenDB.mTaskDao().deleteTask(taskId)
    }

    fun deleteAllTaskByUser(userId: Long){
        mSpargrisenDB.mTaskDao().deleteAllTaskByUser(userId)
    }


//    fun taskWithUserByIconId(iconId: Int): LiveData<List<TaskWithUser>> {
//        return mSpargrisenDB.mTaskWithUserDao().getTaskWithUserByIconId(iconId)
//    }

//    fun insertTaskList(taskList: TaskList){
//        return mSpargrisenDB.mUserWithTaskListAndTasksDao().insertTaskList(taskList)
//    }

    companion object {
        private const val LOG_MSG = "REPO_LOG"
        private const val NUMBER_OF_THREAD = 4
        private var sInstance: DataRepository? = null
        fun getInstance(db: SpargrisenDB, executors: AppExecutors): DataRepository? {
            if(sInstance == null){
                synchronized(DataRepository::class.java){
                    if(sInstance == null){
                        sInstance = DataRepository(db, executors)
                    }
                }
            }
            return sInstance
        }
    }


}